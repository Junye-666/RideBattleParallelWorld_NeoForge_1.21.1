package com.jpigeon.ridebattleparallelworlds.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEffects;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorConstants;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorTeleportUtil;
import com.jpigeon.ridebattleparallelworlds.server.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * 进入镜世界：变身状态下右键反光方块。
 */
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public final class MirrorWorldHandler {
    private MirrorWorldHandler() {
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!MirrorConstants.isMirror(event.getPlayer().level())) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!MirrorConstants.isMirror((net.minecraft.world.level.Level) event.getLevel())) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (MirrorConstants.isMirror(event.getLevel())) {
            event.getDrops().clear();          // 清空所有掉落物
            event.setDroppedExperience(0);     // 经验也不给
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (MirrorConstants.isMirror(event.getLevel())) {
            event.getAffectedBlocks().clear();   // 爆炸不破坏方块
            // 实体伤害保留 —— event.getAffectedEntities() 不动
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // 先处理主世界中尚未完成的镜世界生成任务
        MirrorTeleportUtil.tickGeneration(player);

        // 以下保留原来的镜世界边界处理
        if (!MirrorConstants.isMirror(player.level())) return;

        int yBottom = MirrorTeleportUtil.getYBottom(player);
        if (yBottom == Integer.MIN_VALUE) return;

        int repulsion = MirrorConstants.REPULSION;
        // 掉出反弹地板 → 弹回
        if (player.getY() < yBottom - MirrorConstants.BOUNCE_THRESHOLD) {
            Vec3 known = player.getKnownMovement();
            Vec3 repulse = new Vec3(-known.x(), repulsion, known.z());

            player.setDeltaMovement(repulse);
            player.hurtMarked = true;
        }

        MirrorTeleportUtil.Boundary boundary = MirrorTeleportUtil.getBoundary(player);
        if (boundary != null) {
            BlockPos below = player.blockPosition().below();
            ServerLevel level = player.serverLevel();
            boolean allAir = level.getBlockState(below).isAir()
                    && level.getBlockState(below.below()).isAir()
                    && level.getBlockState(below.below().below()).isAir()
                    && level.getBlockState(below.below().below().below()).isAir()
                    && level.getBlockState(new BlockPos(below.getX(), 0, below.getZ())).isAir();

            if (allAir) {
                double dx = player.getX() - boundary.centerX();
                double dz = player.getZ() - boundary.centerZ();
                double distSq = dx * dx + dz * dz;
                double radiusSq = (double) boundary.radius() * boundary.radius();

                if (distSq > radiusSq) {
                    Vec3 known = player.getKnownMovement();
                    Vec3 repulse = new Vec3(
                            - known.x() * 1.5,
                            0.4,
                            - known.z() *  1.5
                    );

                    player.addDeltaMovement(repulse);
                    player.hurtMarked = true;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // 清理尚未完成的镜世界生成任务
        MirrorTeleportUtil.cancelGeneration(player);

        // 以下保留原来的逻辑
        if (!MirrorConstants.isMirror(player.level())) {
            MirrorTeleportUtil.clearYBottom(player);
            MirrorTeleportUtil.clearBoundary(player);
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // 崩溃/重登后玩家仍在镜世界：检查地形是否还在
        if (MirrorConstants.isMirror(player.level())) {
            ServerLevel mirror = player.serverLevel();
            BlockPos below = player.blockPosition().below();
            boolean terrainExists = !mirror.getBlockState(below).isAir()
                    || !mirror.getBlockState(below.below()).isAir()
                    || !mirror.getBlockState(below.below().below()).isAir();

            if (!terrainExists) {
                // 地形被清空（服务器重启后缓存丢失），传回主世界
                MirrorTeleportUtil.exitMirror(player);
            }
            return;
        }

        // 主世界：清理残留
        MirrorTeleportUtil.clearYBottom(player);
        MirrorTeleportUtil.clearBoundary(player);
    }

    @SubscribeEvent
    public static void onExpired(MobEffectEvent.Expired event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        MobEffectInstance inst = event.getEffectInstance();
        if (inst == null) return;
        if (!inst.getEffect().equals(ModEffects.MIRROR_CORROSION)) return;
        if (!MirrorConstants.isMirror(player.level())) return;

        // 腐蚀到期 → 强制传回主世界
        MirrorTeleportUtil.exitMirror(player);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // 只服务端处理
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // 方块必须是反光方块
        BlockPos pos = event.getPos();

        if (!event.getLevel().getBlockState(pos).is(ModTags.Blocks.REFLECTIVE_BLOCKS)) return;

        // 手上拿着东西不触发
        if (!player.getMainHandItem().isEmpty()) return;

        // 变身状态才可进入
        if (!RideBattleAPI.isTransformed(player)) return;

        // 已在镜世界 -> 退出
        if (MirrorConstants.isMirror(player.level())) {
            MirrorTeleportUtil.exitMirror(player);
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
            return;
        }

        // 主世界 -> 进入
        MirrorTeleportUtil.enterMirror(player, pos);
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }
}
