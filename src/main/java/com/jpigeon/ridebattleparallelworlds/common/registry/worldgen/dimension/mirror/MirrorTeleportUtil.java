package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.MirrorGrassColorSyncPacket;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MirrorTeleportUtil {
    private MirrorTeleportUtil() {
    }

    private static final String TAG_YBOTTOM = "rbpw_mirror_ybottom:";

    private static final String TAG_BOUNDARY = "rbpw_mirror_boundary:";

    public record Boundary(int centerX, int centerZ, int radius) {}

    public static void setBoundary(ServerPlayer player, int centerX, int centerZ, int radius) {
        clearBoundary(player);
        player.addTag(TAG_BOUNDARY + centerX + ":" + centerZ + ":" + radius);
    }

    public static @Nullable Boundary getBoundary(ServerPlayer player) {
        for (String tag : player.getTags()) {
            if (!tag.startsWith(TAG_BOUNDARY)) continue;
            String[] parts = tag.substring(TAG_BOUNDARY.length()).split(":");
            if (parts.length != 3) continue;
            try {
                return new Boundary(Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    public static void clearBoundary(ServerPlayer player) {
        List<String> toRemove = new ArrayList<>();
        for (String tag : player.getTags()) {
            if (tag.startsWith(TAG_BOUNDARY)) toRemove.add(tag);
        }
        for (String tag : toRemove) player.removeTag(tag);
    }

    // ==================== 进入 ====================

    public static void enterMirror(ServerPlayer player, BlockPos entrance) {
        ServerLevel mirror = player.server.getLevel(MirrorConstants.MIRROR_WORLD);
        ServerLevel main = player.server.overworld();
        if (mirror == null) {
            RideBattleParallelWorlds.LOGGER.error("镜世界维度未加载");
            return;
        }
        if (player.getY() < MirrorConstants.yBottom()) {
            player.displayClientMessage(
                    Component.translatable("message.ridebattleparallelworlds.mirror_too_deep"),
                    true
            );
            return;
        }

        // yBottom
        int yBottom = MirrorConstants.yBottom();

        // 逐列对比补齐（统一 X 反转）
        int radius = MirrorConstants.scanRadius() - 1;
        int cx = entrance.getX();
        int cz = entrance.getZ();

        int upperLimit = player.getBlockY() + 128;
        for (int x = cx - radius; x < cx + radius; x++) {
            for (int z = cz - radius; z < cz + radius; z++) {
                scanColumn(main, mirror, x, z, yBottom, upperLimit);
            }
        }

        // 玩家落点（统一 X 反转）
        double mx = -player.getX() + 1;
        double mz = player.getZ();

        player.teleportTo(mirror, mx, player.getY(), mz,
                mirrorYRot(player), player.getXRot());

        player.addEffect(new MobEffectInstance(ModEffects.MIRROR_CORROSION,
                MirrorConstants.CORROSION_DURATION_SECONDS * 20, 0, false, false, true));

        player.addTag(TAG_YBOTTOM + yBottom);
        setBoundary(player, -cx, cz, radius);

        // 采样主世界群系草色并发送
        int grassColor = sampleGrassColor(main, entrance);
        PacketDistributor.sendToPlayer(player, new MirrorGrassColorSyncPacket(grassColor));
    }

    private static int sampleGrassColor(ServerLevel main, BlockPos entrance) {
        var biome = main.getBiome(entrance);
        return biome.value().getGrassColor(entrance.getX(), entrance.getZ());
    }

    // ==================== 退出 ====================

    public static void exitMirror(ServerPlayer player) {
        int yBottom = getYBottom(player);
        ServerLevel main = player.server.overworld();

        if (yBottom == Integer.MIN_VALUE) {
            teleportToSpawn(player, main);
            return;
        }

        double tx = -player.getX() + 1;
        double tz = player.getZ();

        player.teleportTo(main, tx, player.getY(), tz,
                mirrorYRot(player), player.getXRot());

        player.removeEffect(ModEffects.MIRROR_CORROSION);
        clearYBottom(player);
        clearBoundary(player);
    }

    private static float mirrorYRot(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        boolean facingX = Math.abs(look.x) > Math.abs(look.z);
        return facingX ? player.getYRot() : player.getYRot() + 180.0F;
    }

    private static void teleportToSpawn(ServerPlayer player, ServerLevel main) {
        BlockPos spawn = main.getSharedSpawnPos();
        player.teleportTo(main, spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5,
                player.getYRot(), player.getXRot());
        player.removeEffect(ModEffects.MIRROR_CORROSION);
        clearYBottom(player);
        clearBoundary(player);
    }

    // ==================== 逐列对比补齐 ====================

    private static void scanColumn(ServerLevel src, ServerLevel dst, int x, int z, int yBottom, int upperLimit) {
        int mx = -x;

        // 遍历上界：取"主世界顶部"和"镜世界已存在顶部"的最大值
        // → 主世界顶部下降时仍能覆盖到镜世界残楼
        int mainYTop = MirrorScanner.findHighestNonAir(src, x, z, upperLimit);
        int mirrorYTop = MirrorScanner.findHighestNonAir(dst, mx, z, upperLimit);
        int upperBound = Math.max(mainYTop, mirrorYTop);

        if (upperBound == -1) return;   // 两边都空 → 啥都不做

        for (int y = upperBound; y >= yBottom; y--) {
            BlockPos srcPos = new BlockPos(x, y, z);
            BlockPos dstPos = new BlockPos(mx, y, z);
            BlockState s = src.getBlockState(srcPos);
            BlockState m = dst.getBlockState(dstPos);

            // 目标状态
            BlockState desired;
            if (s.isAir() || !MirrorScanner.isExposed(src, x, y, z)) {
                // 主世界 air / 非暴露 → 镜世界必须 air
                desired = Blocks.AIR.defaultBlockState();
            } else {
                // 主世界有方块 + 暴露 → 镜像
                desired = MirrorScanner.mirrorBlockState(s);
            }

            if (!m.equals(desired)) {
                dst.setBlock(dstPos, desired, 3 | 16);   // 19 = 保留光照/客户端同步，跳过形状更新
            }
        }

        ensureBounceFloor(dst, mx, z, yBottom);
    }

    /**
     * yBottom 处是 air 才铺玻璃（主世界有方块的地方不覆盖）
     */
    private static void ensureBounceFloor(ServerLevel dst, int mx, int z, int yBottom) {
        BlockPos pos = new BlockPos(mx, yBottom, z);
        if (dst.getBlockState(pos).isAir()) {
            dst.setBlock(pos, Blocks.GLASS.defaultBlockState(), 3 | 16);
        }
    }

    // ==================== YBottom Tag ====================

    public static int getYBottom(ServerPlayer player) {
        for (String tag : player.getTags()) {
            if (!tag.startsWith(TAG_YBOTTOM)) continue;
            try {
                return Integer.parseInt(tag.substring(TAG_YBOTTOM.length()));
            } catch (NumberFormatException ignored) {
            }
        }
        return Integer.MIN_VALUE;
    }

    public static void clearYBottom(ServerPlayer player) {
        List<String> toRemove = new ArrayList<>();
        for (String tag : player.getTags()) {
            if (tag.startsWith(TAG_YBOTTOM)) toRemove.add(tag);
        }
        for (String tag : toRemove) player.removeTag(tag);
    }
}