package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.MirrorGrassColorSyncPacket;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MirrorTeleportUtil {
    private MirrorTeleportUtil() {
    }

    private static final String TAG_YBOTTOM = "rbpw_mirror_ybottom:";

    private static final String TAG_BOUNDARY = "rbpw_mirror_boundary:";

    // ==================== 镜世界生成任务 ====================

    // 玩家 UUID -> 对应的镜世界生成任务
    // 所有访问都发生在服务端主线程，不需要 ConcurrentHashMap
    private static final Map<UUID, MirrorGenerationJob> GENERATION_JOBS =
            new HashMap<>();

    // 每个服务端 Tick 最多使用约 3ms 进行镜世界生成
    // 多名玩家同时生成时，共享这个预算
    private static final long MAX_GENERATION_NANOS_PER_TICK = 3_000_000L;

    // 单个玩家每 Tick 最多扫描的列数，额外限制工作量
    private static final int MAX_COLUMNS_PER_PLAYER_PER_TICK = 512;

    private static MinecraftServer generationBudgetServer;
    private static long generationBudgetTick = Long.MIN_VALUE;
    private static long generationBudgetRemainingNanos;

    public record Boundary(int centerX, int centerZ, int radius) {
    }

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
            } catch (NumberFormatException ignored) {
            }
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

        // 当前版本只允许从主世界进入
        if (player.serverLevel() != main) {
            player.displayClientMessage(
                    Component.literal("镜世界目前只支持从主世界进入"),
                    true
            );
            return;
        }

        int yBottom = MirrorConstants.yBottom();

        if (player.getY() < yBottom) {
            player.displayClientMessage(
                    Component.translatable(
                            "message.ridebattleparallelworlds.mirror_too_deep"
                    ),
                    true
            );
            return;
        }

        // 防止同一玩家重复创建生成任务
        if (GENERATION_JOBS.containsKey(player.getUUID())) {
            return;
        }

        // 逐列对比补齐（统一 X 反转）
        int radius = MirrorConstants.scanRadius() - 1;

        int cx = entrance.getX();
        int cz = entrance.getZ();

        int upperLimit = player.getBlockY() + 128;

        // 创建任务，不再立即执行完整扫描
        MirrorGenerationJob job = new MirrorGenerationJob(
                player,
                main,
                mirror,
                entrance,
                yBottom,
                upperLimit,
                cx,
                cz,
                radius
        );

        GENERATION_JOBS.put(player.getUUID(), job);

        player.displayClientMessage(
                Component.literal("镜世界正在映射，请稍候……"),
                true
        );
    }

    /**
     * 每个玩家 Tick 调用一次。
     * <p>
     * 将镜世界生成任务拆分到多个 Tick 中执行，
     * 避免一次性扫描造成服务器卡顿。
     */
    public static void tickGeneration(ServerPlayer player) {

        MirrorGenerationJob job = GENERATION_JOBS.get(player.getUUID());

        if (job == null) {
            return;
        }

        // 玩家对象发生变化、离开主世界或实体被移除
        // 直接取消任务，避免旧任务继续操作
        if (job.player != player
                || player.isRemoved()
                || player.serverLevel() != job.main) {

            GENERATION_JOBS.remove(player.getUUID());
            return;
        }

        // 全部玩家共享同一个服务端 Tick 时间预算
        MinecraftServer server = player.server;
        long currentTick = server.getTickCount();

        if (generationBudgetServer != server
                || generationBudgetTick != currentTick) {

            generationBudgetServer = server;
            generationBudgetTick = currentTick;

            generationBudgetRemainingNanos =
                    MAX_GENERATION_NANOS_PER_TICK;
        }

        int processed = 0;

        try {

            while (job.hasNext()
                    && processed < MAX_COLUMNS_PER_PLAYER_PER_TICK
                    && generationBudgetRemainingNanos > 0) {

                long startTime = System.nanoTime();

                // 执行一列镜像生成
                scanColumn(
                        job.main,
                        job.mirror,
                        job.currentX,
                        job.currentZ,
                        job.yBottom,
                        job.upperLimit
                );

                long elapsed = System.nanoTime() - startTime;

                // 更新本 Tick 剩余预算
                generationBudgetRemainingNanos -=
                        Math.max(1L, elapsed);

                job.advance();
                processed++;
            }

        } catch (RuntimeException e) {

            GENERATION_JOBS.remove(player.getUUID());

            RideBattleParallelWorlds.LOGGER.error(
                    "镜世界生成失败：玩家 {}",
                    player.getName().getString(),
                    e
            );

            player.displayClientMessage(
                    Component.literal("镜世界生成失败，请检查服务端日志"),
                    true
            );

            return;
        }

        // 所有列生成完成
        if (!job.hasNext()) {

            GENERATION_JOBS.remove(player.getUUID());

            completeMirrorEntry(job);
        }
    }

    /**
     * 玩家离线时取消尚未完成的生成任务。
     */
    public static void cancelGeneration(ServerPlayer player) {
        GENERATION_JOBS.remove(player.getUUID());
    }

    /**
     * 生成完成后执行真正的传送。
     */
    private static void completeMirrorEntry(MirrorGenerationJob job) {

        ServerPlayer player = job.player;

        if (player.isRemoved() || player.serverLevel() != job.main) {
            return;
        }

        // 玩家落点，统一 X 反转
        double mx = -player.getX() + 1;
        double mz = player.getZ();

        player.teleportTo(
                job.mirror,
                mx,
                player.getY(),
                mz,
                mirrorYRot(player),
                player.getXRot()
        );

        // 镜世界腐蚀计时
        player.addEffect(new MobEffectInstance(
                ModEffects.MIRROR_CORROSION,
                MirrorConstants.CORROSION_DURATION_SECONDS * 20,
                0,
                false,
                false,
                true
        ));

        // 记录镜世界底部
        player.addTag(TAG_YBOTTOM + job.yBottom);

        // 记录镜世界区域边界
        setBoundary(
                player,
                -job.cx,
                job.cz,
                job.radius
        );

        // 采样入口处的主世界草地颜色
        int grassColor = sampleGrassColor(
                job.main,
                job.entrance
        );

        PacketDistributor.sendToPlayer(
                player,
                new MirrorGrassColorSyncPacket(grassColor)
        );
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

    /**
     * 镜世界地形生成任务。
     * <p>
     * 按 X、Z 两个方向逐列扫描，
     * 每个 Tick 由 tickGeneration() 决定执行多少列。
     */
    private static final class MirrorGenerationJob {

        private final ServerPlayer player;

        private final ServerLevel main;
        private final ServerLevel mirror;

        private final BlockPos entrance;

        private final int yBottom;
        private final int upperLimit;

        private final int cx;
        private final int cz;
        private final int radius;

        private int currentX;
        private int currentZ;

        private MirrorGenerationJob(
                ServerPlayer player,
                ServerLevel main,
                ServerLevel mirror,
                BlockPos entrance,
                int yBottom,
                int upperLimit,
                int cx,
                int cz,
                int radius
        ) {

            this.player = player;

            this.main = main;
            this.mirror = mirror;

            this.entrance = new BlockPos(
                    entrance.getX(),
                    entrance.getY(),
                    entrance.getZ()
            );

            this.yBottom = yBottom;
            this.upperLimit = upperLimit;

            this.cx = cx;
            this.cz = cz;
            this.radius = radius;

            this.currentX = cx - radius;
            this.currentZ = cz - radius;
        }

        /**
         * 是否还有待处理的列。
         */
        private boolean hasNext() {
            return currentX < cx + radius;
        }

        /**
         * 移动到下一列。
         */
        private void advance() {

            currentZ++;

            if (currentZ >= cz + radius) {

                currentZ = cz - radius;

                currentX++;
            }
        }
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