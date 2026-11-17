package com.jpigeon.ridebattleparallelworlds.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEffects;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorActiveRegionManager;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorConstants;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorTeleportUtil;
import com.jpigeon.ridebattleparallelworlds.server.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
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
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * 镜世界事件入口。
 * <p>
 * 这里只处理事件与玩家生命周期；
 * 映射、传送和区域状态由各自管理类负责。
 */
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public final class MirrorWorldHandler {

    private MirrorWorldHandler() {
    }

    // =================== 镜世界世界规则 ===================
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!MirrorConstants.isMirror(event.getPlayer().level())) {
            return;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof Level level)) {
            return;
        }

        if (!MirrorConstants.isMirror(level)) {
            return;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!MirrorConstants.isMirror(event.getLevel())) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExperience(0);
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (!MirrorConstants.isMirror(event.getLevel())) {
            return;
        }

        // 只禁止方块破坏，保留实体伤害。
        event.getAffectedBlocks().clear();
    }

    // =================== 全服映射任务调度 ===================

    /**
     * 映射工作由全服统一调度。
     * <p>
     * 不再让每个玩家的 PlayerTick 分别抢占全服预算。
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MirrorTeleportUtil.tickGeneration(event.getServer());
    }

    // =================== 玩家 Tick：底部保护与映射边界 ===================

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!MirrorConstants.isMirror(player.level())) {
            return;
        }

        ServerLevel main = player.server.overworld();

        // 底部保护
        int yBottom = MirrorTeleportUtil.getYBottom(player);

        if (yBottom != Integer.MIN_VALUE
                && player.getY()
                < yBottom - MirrorConstants.BOUNCE_THRESHOLD) {

            Vec3 known = player.getKnownMovement();

            Vec3 repulse = new Vec3(
                    -known.x(),
                    MirrorConstants.REPULSION,
                    known.z()
            );

            player.setDeltaMovement(repulse);
            player.hurtMarked = true;
        }

        // 映射覆盖区域判定
        BlockPos currentPos = player.blockPosition();

        boolean mapped = MirrorTeleportUtil.isMirrorColumnMapped(
                main,
                currentPos.getX(),
                currentPos.getZ()
        );

        if (mapped) {
            MirrorTeleportUtil.rememberMappedPosition(player);
            return;
        }

        // 玩家进入未映射区域
        Vec3 lastMapped =
                MirrorTeleportUtil.getLastMappedPosition(player);

        if (lastMapped == null) {
            if (Config.debug()) {
                RideBattleParallelWorlds.LOGGER.warn(
                        "Mirror boundary recovery: player={}, currentPosition={}, missingLastMappedPosition",
                        player.getName().getString(),
                        currentPos
                );
            }

            MirrorTeleportUtil.exitMirror(player);
            return;
        }

        // 水平回返
        double dx = lastMapped.x - player.getX();
        double dz = lastMapped.z - player.getZ();

        Vec3 direction = new Vec3(dx, 0.0D, dz);

        Vec3 horizontalCorrection;

        if (direction.lengthSqr() > 0.0001D) {
            horizontalCorrection = direction
                    .normalize()
                    .scale(0.65D);
        } else {
            horizontalCorrection = Vec3.ZERO;
        }

        // 只修改水平速度，保留垂直速度。
        Vec3 currentMovement = player.getDeltaMovement();

        player.setDeltaMovement(
                horizontalCorrection.x(),
                currentMovement.y(),
                horizontalCorrection.z()
        );

        player.hurtMarked = true;
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // 先取消未完成的任务，释放生成预留。
        MirrorTeleportUtil.cancelGeneration(player);

        if (MirrorConstants.isMirror(player.level())) {
            /*
             * 登出时释放运行时引用，但保留玩家持久化区域 Tag。
             *
             * 玩家重新登录且仍在镜世界时，
             * onLogin 会恢复这些引用。
             */
            MirrorActiveRegionManager.releaseForLogout(player);

            RideBattleParallelWorlds.LOGGER.info(
                    "Mirror player logged out: player={}, persistentRegionChunks={}",
                    player.getName().getString(),
                    MirrorActiveRegionManager.readPersistentRegion(player).size()
            );

            return;
        }

        // 主世界登出：清理可能残留的镜世界状态。
        MirrorActiveRegionManager.releasePlayerRegion(
                player,
                true,
                "logout-outside-mirror"
        );

        player.removeEffect(ModEffects.MIRROR_CORROSION);

        MirrorTeleportUtil.clearYBottom(player);
        MirrorTeleportUtil.clearBoundary(player);
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!MirrorConstants.isMirror(player.level())) {
            // 玩家实际上在主世界，清理可能残留的镜世界状态。
            MirrorActiveRegionManager.releasePlayerRegion(
                    player,
                    true,
                    "login-outside-mirror"
            );

            player.removeEffect(ModEffects.MIRROR_CORROSION);

            MirrorTeleportUtil.clearYBottom(player);
            MirrorTeleportUtil.clearBoundary(player);
            return;
        }


         // 必须先恢复活跃区域引用。
         //  失败时踢出镜世界，
        if (!MirrorActiveRegionManager.restorePlayerRegion(player)) {
            RideBattleParallelWorlds.LOGGER.error(
                    "Mirror login recovery failed: player={}, no active region",
                    player.getName().getString()
            );

            MirrorTeleportUtil.exitMirror(player);
            return;
        }

        ServerLevel main = player.server.overworld();
        BlockPos currentPos = player.blockPosition();

        boolean mapped = MirrorTeleportUtil.isMirrorColumnMapped(
                main,
                currentPos.getX(),
                currentPos.getZ()
        );

        if (mapped) {
            MirrorTeleportUtil.rememberMappedPosition(player);
            return;
        }

        // 有最后有效位置时，交由 PlayerTick 的边界逻辑回返。
        if (MirrorTeleportUtil.getLastMappedPosition(player) != null) {
            return;
        }

        RideBattleParallelWorlds.LOGGER.warn(
                "Mirror login recovery: player={}, currentPosition={}, missingLastMappedPosition",
                player.getName().getString(),
                currentPos
        );

        MirrorTeleportUtil.exitMirror(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (MirrorConstants.isMirror(player.level())) {
            /*
             * 重生后替换旧玩家的运行时区域引用。
             *
             * 同一服务端进程内如果持久化 Tag 未复制，
             * Manager 仍可使用原 UUID 的运行时集合恢复。
             */
            if (!MirrorActiveRegionManager.restorePlayerRegion(player)) {
                RideBattleParallelWorlds.LOGGER.error(
                        "Mirror respawn recovery failed: player={}, no active region",
                        player.getName().getString()
                );

                MirrorTeleportUtil.exitMirror(player);
            }

            return;
        }

        // 重生到镜世界以外，释放引用并清除镜世界状态。
        MirrorActiveRegionManager.releasePlayerRegion(
                player,
                true,
                "respawn-outside-mirror"
        );

        player.removeEffect(ModEffects.MIRROR_CORROSION);

        MirrorTeleportUtil.clearYBottom(player);
        MirrorTeleportUtil.clearBoundary(player);
    }

    // 腐蚀效果到期
    @SubscribeEvent
    public static void onExpired(MobEffectEvent.Expired event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        MobEffectInstance instance = event.getEffectInstance();

        if (instance == null) {
            return;
        }

        if (!instance.getEffect().equals(ModEffects.MIRROR_CORROSION)) {
            return;
        }

        if (!MirrorConstants.isMirror(player.level())) {
            return;
        }

        RideBattleParallelWorlds.LOGGER.info(
                "Mirror corrosion expired: player={}, forcing exit",
                player.getName().getString()
        );

        MirrorTeleportUtil.exitMirror(player);
    }

    // 反光入口交互
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        // 只在服务端处理。
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        BlockPos pos = event.getPos();

        if (!event.getLevel()
                .getBlockState(pos)
                .is(ModTags.Blocks.REFLECTIVE_BLOCKS)) {
            return;
        }

        // 手持物品时不触发。
        if (!player.getMainHandItem().isEmpty()) {
            return;
        }

        // 只有变身状态下才能进入/退出。
        if (!RideBattleAPI.isTransformed(player)) {
            return;
        }

        if (MirrorConstants.isMirror(player.level())) {
            MirrorTeleportUtil.exitMirror(player);

            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
            return;
        }

        MirrorTeleportUtil.enterMirror(player, pos);

        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }
}