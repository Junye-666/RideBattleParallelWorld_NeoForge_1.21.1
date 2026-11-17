package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.MirrorGrassColorSyncPacket;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

/**
 * 镜世界入口、映射、扫描、传送和边界状态。
 * <p>
 * 镜世界是由主世界入口驱动的有限快照空间。
 * <p>
 * 所有方法均在服务端主线程调用。
 */
public final class MirrorTeleportUtil {
    private MirrorTeleportUtil() {
    }

    private static final String TAG_YBOTTOM = "rbpw_mirror_ybottom:";

    private static final String TAG_LAST_MAPPED_POSITION = "rbpw_mirror_last_mapped:";

    private static final String TAG_LEGACY_BOUNDARY = "rbpw_mirror_boundary:";

    private static final int DEFAULT_GRASS_COLOR = 0x91BD59;

    /**
     * 玩家 UUID -> 映射任务。
     * <p>
     * 只在服务端主线程访问。
     */
    private static final Map<UUID, MirrorGenerationJob> GENERATION_JOBS =
            new HashMap<>();

    /**
     * 待执行任务的轮转队列。
     * <p>
     * 每个任务在队列中最多出现一次。
     */
    private static final Deque<UUID> GENERATION_QUEUE =
            new ArrayDeque<>();

    /**
     * 每个服务端 Tick 最多使用约 3ms 映射时间。
     */
    private static final long MAX_GENERATION_NANOS_PER_TICK =
            3_000_000L;

    /**
     * 查询镜世界某列是否已经映射。
     * <p>
     * 镜世界 X 转回主世界 X 后查询覆盖记录。
     */
    public static boolean isMirrorColumnMapped(
            ServerLevel main,
            int mirrorX,
            int mirrorZ
    ) {
        int sourceX =
                MirrorReflectionTransform.mirrorBlockX(mirrorX);

        return MirrorCoverageData.isColumnMapped(
                main,
                sourceX,
                mirrorZ
        );
    }

    /**
     * 记录玩家最后一次处于有效映射列的位置。
     * <p>
     * 只记录 X/Z，不记录 Y。
     */
    public static void rememberMappedPosition(ServerPlayer player) {
        BlockPos pos = player.blockPosition();

        String tag = TAG_LAST_MAPPED_POSITION
                + pos.getX() + ":" + pos.getZ();

        if (player.getTags().contains(tag)) {
            return;
        }

        clearBoundary(player);
        player.addTag(tag);
    }

    /**
     * 获取玩家最后一次处于有效映射区域的位置。
     */
    public static Vec3 getLastMappedPosition(ServerPlayer player) {
        for (String tag : player.getTags()) {
            if (!tag.startsWith(TAG_LAST_MAPPED_POSITION)) {
                continue;
            }

            String[] parts = tag.substring(
                    TAG_LAST_MAPPED_POSITION.length()
            ).split(":");

            if (parts.length != 2) {
                continue;
            }

            try {
                int x = Integer.parseInt(parts[0]);
                int z = Integer.parseInt(parts[1]);

                return new Vec3(
                        x + 0.5D,
                        0.0D,
                        z + 0.5D
                );
            } catch (NumberFormatException ignored) {
                // 忽略损坏的旧标签。
            }
        }

        return null;
    }

    /**
     * 清理最后有效位置和旧版本 Boundary 标签。
     */
    public static void clearBoundary(ServerPlayer player) {
        List<String> toRemove = new ArrayList<>();

        for (String tag : player.getTags()) {
            if (tag.startsWith(TAG_LAST_MAPPED_POSITION)
                    || tag.startsWith(TAG_LEGACY_BOUNDARY)) {
                toRemove.add(tag);
            }
        }

        for (String tag : toRemove) {
            player.removeTag(tag);
        }
    }

    /**
     * 单列任务。
     * <p>
     * fullScan=true：首次完整映射。
     * fullScan=false：已有列 Dirty 增量刷新。
     */
    private record ColumnTask(
            int x,
            int z,
            boolean fullScan
    ) {
    }

    /**
     * 一次入口请求的映射计划。
     */
    private record MirrorGenerationPlan(
            List<ColumnTask> tasks,
            Map<LevelChunk, Long> dirtySnapshots,
            Set<Long> regionChunks,
            Set<Long> blockedActiveChunks,
            int requestedColumns,
            int newColumns,
            int dirtyColumns,
            int deferredDirtyChunks
    ) {
    }

    /**
     * 计划构建期间缓存源 Chunk。
     */
    private static LevelChunk getCachedChunk(
            ServerLevel level,
            int blockX,
            int blockZ,
            Map<Long, LevelChunk> chunkCache
    ) {
        int chunkX = blockX >> 4;
        int chunkZ = blockZ >> 4;

        long key = MirrorActiveRegionManager.chunkKey(
                chunkX,
                chunkZ
        );

        return chunkCache.computeIfAbsent(
                key,
                ignored -> level.getChunk(chunkX, chunkZ)
        );
    }

    private static boolean isColumnMapped(
            ServerLevel main,
            int x,
            int z,
            Map<Long, LevelChunk> chunkCache
    ) {
        LevelChunk chunk = getCachedChunk(
                main,
                x,
                z,
                chunkCache
        );

        var coverageType = PWAttachments.MIRROR_COVERAGE.get();

        if (!chunk.hasData(coverageType)) {
            return false;
        }

        MirrorCoverageData coverage =
                chunk.getData(coverageType);

        return coverage.isColumnMapped(
                x & 15,
                z & 15
        );
    }

    /**
     * 将已映射列加入 Dirty 刷新任务。
     * <p>
     * 首次映射任务优先，不会被增量任务覆盖。
     */
    private static void addMappedDirtyTask(
            ServerLevel main,
            Map<Long, ColumnTask> tasks,
            Map<Long, LevelChunk> chunkCache,
            int x,
            int z
    ) {
        if (!isColumnMapped(main, x, z, chunkCache)) {
            return;
        }

        long key = columnKey(x, z);

        tasks.putIfAbsent(
                key,
                new ColumnTask(x, z, false)
        );
    }

    private static long columnKey(int x, int z) {
        return new BlockPos(x, 0, z).asLong();
    }

    /**
     * 根据实际扫描坐标范围计算涉及的 Source Chunk 集合。
     * <p>
     * maxX/maxZ 为 exclusive。
     */
    private static Set<Long> collectSourceChunkKeys(
            int minX,
            int maxXExclusive,
            int minZ,
            int maxZExclusive
    ) {
        Set<Long> result = new HashSet<>();

        if (maxXExclusive <= minX
                || maxZExclusive <= minZ) {
            return result;
        }

        int minChunkX = Math.floorDiv(minX, 16);
        int maxChunkX = Math.floorDiv(maxXExclusive - 1, 16);

        int minChunkZ = Math.floorDiv(minZ, 16);
        int maxChunkZ = Math.floorDiv(maxZExclusive - 1, 16);

        for (int chunkX = minChunkX;
             chunkX <= maxChunkX;
             chunkX++) {

            for (int chunkZ = minChunkZ;
                 chunkZ <= maxChunkZ;
                 chunkZ++) {

                result.add(
                        MirrorActiveRegionManager.chunkKey(
                                chunkX,
                                chunkZ
                        )
                );
            }
        }

        return result;
    }

    // ======================== 映射计划构建 ========================

    /**
     * 创建一次入口请求的映射计划。
     * <p>
     * 只将未映射列和 Dirty 影响列加入任务。
     */
    private static MirrorGenerationPlan buildGenerationPlan(
            ServerLevel main,
            int centerX,
            int centerZ,
            int radius
    ) {
        LinkedHashMap<Long, ColumnTask> tasks =
                new LinkedHashMap<>();

        Map<LevelChunk, Long> dirtySnapshots =
                new LinkedHashMap<>();

        Map<Long, LevelChunk> chunkCache =
                new HashMap<>();

        Set<Long> blockedActiveChunks =
                new HashSet<>();

        Set<Long> deferredDirtySourceChunks =
                new HashSet<>();

        int minX = centerX - radius;
        int maxXExclusive = centerX + radius;

        int minZ = centerZ - radius;
        int maxZExclusive = centerZ + radius;

        Set<Long> regionChunks = new HashSet<>(
                collectSourceChunkKeys(
                        minX,
                        maxXExclusive,
                        minZ,
                        maxZExclusive
                )
        );

        // 首次映射区域
        int newColumns = 0;

        for (int x = minX; x < maxXExclusive; x++) {
            for (int z = minZ; z < maxZExclusive; z++) {

                if (isColumnMapped(main, x, z, chunkCache)) {
                    continue;
                }

                long sourceChunkKey =
                        MirrorActiveRegionManager.chunkKey(
                                x >> 4,
                                z >> 4
                        );

                // 活跃区域不能补充首次映射。
                if (MirrorActiveRegionManager.isChunkActive(
                        sourceChunkKey
                )) {
                    blockedActiveChunks.add(sourceChunkKey);
                    continue;
                }

                tasks.put(
                        columnKey(x, z),
                        new ColumnTask(x, z, true)
                );

                newColumns++;
            }
        }

        // 收集入口附近 Dirty 源 Chunk
        var dirtyType = PWAttachments.MIRROR_DIRTY.get();

        int minChunkX = Math.floorDiv(minX - 1, 16);
        int maxChunkX = Math.floorDiv(maxXExclusive, 16);

        int minChunkZ = Math.floorDiv(minZ - 1, 16);
        int maxChunkZ = Math.floorDiv(maxZExclusive, 16);

        final int[] offsetX = {0, 1, -1, 0, 0};
        final int[] offsetZ = {0, 0, 0, 1, -1};

        for (int chunkX = minChunkX;
             chunkX <= maxChunkX;
             chunkX++) {

            for (int chunkZ = minChunkZ;
                 chunkZ <= maxChunkZ;
                 chunkZ++) {

                LevelChunk dirtyChunk = main.getChunk(
                        chunkX,
                        chunkZ
                );

                if (!dirtyChunk.hasData(dirtyType)) {
                    continue;
                }

                MirrorDirtyData dirty =
                        dirtyChunk.getData(dirtyType);

                boolean fullRescan = dirty.needsFullRescan();

                // 位置只取一次快照，后续遍历复用。
                Set<BlockPos> dirtyPositions =
                        fullRescan
                                ? Set.of()
                                : dirty.getDirtyPositions();

                if (!fullRescan && dirtyPositions.isEmpty()) {
                    continue;
                }

                long dirtySourceKey =
                        MirrorActiveRegionManager.chunkKey(
                                chunkX,
                                chunkZ
                        );

                regionChunks.add(dirtySourceKey);

                // Dirty 来源 Chunk 正被使用时，整份 Dirty 延迟处理。
                if (MirrorActiveRegionManager.isChunkActive(
                        dirtySourceKey
                )) {
                    deferredDirtySourceChunks.add(dirtySourceKey);
                    continue;
                }

                boolean deferThisSnapshot = false;

                // Dirty 超限：完整重扫该源 Chunk 已覆盖列
                if (fullRescan) {
                    int baseX = chunkX << 4;
                    int baseZ = chunkZ << 4;

                    for (int localX = 0; localX < 16; localX++) {
                        for (int localZ = 0; localZ < 16; localZ++) {

                            int x = baseX + localX;
                            int z = baseZ + localZ;

                            if (!isColumnMapped(
                                    main,
                                    x,
                                    z,
                                    chunkCache
                            )) {
                                continue;
                            }

                            long candidateChunkKey =
                                    MirrorActiveRegionManager.chunkKey(
                                            x >> 4,
                                            z >> 4
                                    );

                            regionChunks.add(candidateChunkKey);

                            if (MirrorActiveRegionManager.isChunkActive(
                                    candidateChunkKey
                            )) {
                                deferThisSnapshot = true;
                                continue;
                            }

                            addMappedDirtyTask(
                                    main,
                                    tasks,
                                    chunkCache,
                                    x,
                                    z
                            );
                        }
                    }
                } else {
                    // Dirty：自身 + 四个水平邻列

                    for (BlockPos dirtyPos : dirtyPositions) {
                        int x = dirtyPos.getX();
                        int z = dirtyPos.getZ();

                        for (int i = 0; i < offsetX.length; i++) {
                            int candidateX = x + offsetX[i];
                            int candidateZ = z + offsetZ[i];

                            if (!isColumnMapped(
                                    main,
                                    candidateX,
                                    candidateZ,
                                    chunkCache
                            )) {
                                continue;
                            }

                            long candidateChunkKey =
                                    MirrorActiveRegionManager.chunkKey(
                                            candidateX >> 4,
                                            candidateZ >> 4
                                    );

                            regionChunks.add(candidateChunkKey);

                            // 活跃 Chunk 的 Dirty 列不能刷新。
                            if (MirrorActiveRegionManager.isChunkActive(
                                    candidateChunkKey
                            )) {
                                deferThisSnapshot = true;
                                continue;
                            }

                            addMappedDirtyTask(
                                    main,
                                    tasks,
                                    chunkCache,
                                    candidateX,
                                    candidateZ
                            );
                        }
                    }
                }

                // Dirty revision 快照
                if (deferThisSnapshot) {
                    // 只要有一个影响列被活跃区域保护，
                    // 整个 Dirty 源 Chunk 都保留。
                    deferredDirtySourceChunks.add(dirtySourceKey);
                } else {
                    dirtySnapshots.put(
                            dirtyChunk,
                            dirty.getRevision()
                    );
                }
            }
        }

        // 统计映射
        int dirtyColumns = 0;

        for (ColumnTask task : tasks.values()) {
            if (!task.fullScan()) {
                dirtyColumns++;
            }
        }

        int requestedColumns =
                (maxXExclusive - minX)
                        * (maxZExclusive - minZ);

        return new MirrorGenerationPlan(
                List.copyOf(tasks.values()),
                Map.copyOf(dirtySnapshots),
                Set.copyOf(regionChunks),
                Set.copyOf(blockedActiveChunks),
                requestedColumns,
                newColumns,
                dirtyColumns,
                deferredDirtySourceChunks.size()
        );
    }

    // 进入镜世界
    public static void enterMirror(ServerPlayer player, BlockPos entrance) {
        ServerLevel main = player.server.overworld();

        ServerLevel mirror =
                player.server.getLevel(MirrorConstants.MIRROR_WORLD);

        if (mirror == null) {
            player.displayClientMessage(
                    Component.literal("镜世界当前不可用，请联系管理员"),
                    true
            );
            return;
        }

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

        UUID playerId = player.getUUID();

        if (GENERATION_JOBS.containsKey(playerId)) {
            return;
        }

        // 维持原配置的扫描范围语义。
        int radius = MirrorConstants.scanRadius() - 1;

        if (radius < 1) {
            RideBattleParallelWorlds.LOGGER.error(
                    "Mirror entry rejected: invalid scan radius={}",
                    radius
            );
            return;
        }

        int centerX = entrance.getX();
        int centerZ = entrance.getZ();

        int upperLimit = main.getMaxBuildHeight() - 1;

        long planStarted = System.nanoTime();

        MirrorGenerationPlan plan = buildGenerationPlan(
                main,
                centerX,
                centerZ,
                radius
        );

        long planElapsed = System.nanoTime() - planStarted;

        // 活跃区域保护
        if (!plan.blockedActiveChunks().isEmpty()) {
            player.displayClientMessage(
                    Component.literal(
                            "目标镜像区域仍有玩家正在使用，请等待对方离开后再次进入"
                    ),
                    true
            );

            return;
        }

        // 预留本次请求涉及的非活跃 Chunk
        Set<Long> reservationChunks = new HashSet<>();

        for (long key : plan.regionChunks()) {
            if (!MirrorActiveRegionManager.isChunkActive(key)) {
                reservationChunks.add(key);
            }
        }

        boolean reserved =
                MirrorActiveRegionManager.tryReserve(
                        playerId,
                        reservationChunks
                );

        if (!reserved) {
            if (Config.debug()) {
                RideBattleParallelWorlds.LOGGER.info(
                        "Mirror entry deferred by generation lock: player={}, entrance={}, chunks={}",
                        player.getName().getString(),
                        entrance,
                        reservationChunks.size()
                );
            }

            player.displayClientMessage(
                    Component.literal(
                            "目标镜像区域正在由其他入口生成，请稍后再次尝试。"
                    ),
                    true
            );

            return;
        }

        // 创建生成任务
        MirrorGenerationJob job = new MirrorGenerationJob(
                player,
                main,
                mirror,
                entrance,
                yBottom,
                upperLimit,
                plan
        );

        // 无需同步：直接进入
        if (!job.hasNext()) {
            clearDirtySnapshots(job);
            completeMirrorEntry(job);
            return;
        }

        // 放入全服轮转队列

        GENERATION_JOBS.put(playerId, job);
        GENERATION_QUEUE.addLast(playerId);

        if (Config.debug()) {
            RideBattleParallelWorlds.LOGGER.info(
                    "Mirror mapping queued: player={}, entrance={}, requestedColumns={}, newColumns={}, dirtyColumns={}, totalTasks={}, dirtySourceChunks={}, deferredDirtyChunks={}, reservedChunks={}, queueSize={}, planMs={}",
                    player.getName().getString(),
                    entrance,
                    plan.requestedColumns(),
                    plan.newColumns(),
                    plan.dirtyColumns(),
                    plan.tasks().size(),
                    plan.dirtySnapshots().size(),
                    plan.deferredDirtyChunks(),
                    reservationChunks.size(),
                    GENERATION_QUEUE.size(),
                    planElapsed / 1_000_000.0
            );
        }

        String message =
                plan.dirtyColumns() > 0 || plan.newColumns() == 0
                        ? "镜世界正在更新变化区域……"
                        : "镜世界正在首次映射……";

        player.displayClientMessage(
                Component.literal(message),
                true
        );
    }

    /**
     * 只有 revision 未变化时才清除 Dirty。
     * <p>
     * 同步期间出现的新修改不会被错误清除。
     */
    private static int clearDirtySnapshots(MirrorGenerationJob job) {
        var dirtyType = PWAttachments.MIRROR_DIRTY.get();

        int cleared = 0;

        for (Map.Entry<LevelChunk, Long> entry
                : job.dirtySnapshots.entrySet()) {

            LevelChunk chunk = entry.getKey();
            long expectedRevision = entry.getValue();

            MirrorDirtyData dirty = chunk.getData(dirtyType);

            // revision 不一致时不清除，保留最新 Dirty。
            if (dirty.getRevision() != expectedRevision) {
                continue;
            }

            dirty.clearAfterSync();
            chunk.setUnsaved(true);
            cleared++;
        }

        return cleared;
    }

    /**
     * 每个服务端 Tick 调用一次。
     * <p>
     * 不再从每个玩家的 PlayerTick 分别争抢预算。
     * <p>
     * 每轮只处理一个玩家的一列，然后放回队尾。
     * 多个玩家因此可以公平共享全服映射预算。
     */
    public static void tickGeneration(MinecraftServer server) {
        if (GENERATION_QUEUE.isEmpty()) {
            return;
        }

        long deadline =
                System.nanoTime() + MAX_GENERATION_NANOS_PER_TICK;

        boolean processedAny = false;

        while (!GENERATION_QUEUE.isEmpty()
                && (!processedAny || System.nanoTime() < deadline)) {

            UUID playerId = GENERATION_QUEUE.removeFirst();

            MirrorGenerationJob job =
                    GENERATION_JOBS.get(playerId);

            // 可能是取消任务后遗留的队列项。
            if (job == null) {
                continue;
            }

            ServerPlayer player = job.player;

            // 玩家状态失效
            if (player.isRemoved()
                    || player.server != server
                    || player.serverLevel() != job.main) {

                abortGeneration(playerId, job);

                processedAny = true;
                continue;
            }

            // 执行一列

            ColumnTask task = job.currentTask();

            try {
                scanColumn(
                        job.main,
                        job.mirror,
                        task.x(),
                        task.z(),
                        job.yBottom,
                        job.upperLimit
                );

                // 只有完整首次映射列才标记 Coverage。
                if (task.fullScan()) {
                    MirrorCoverageData.markColumnMapped(
                            job.main,
                            task.x(),
                            task.z(),
                            job.yBottom
                    );
                }

                job.recordCompleted(task);
                job.advance();

            } catch (RuntimeException e) {
                abortGeneration(playerId, job);

                RideBattleParallelWorlds.LOGGER.error(
                        "Mirror mapping failed: player={}, entrance={}, completed={}, total={}, remaining={}",
                        player.getName().getString(),
                        job.entrance,
                        job.completedColumns,
                        job.tasks.size(),
                        job.tasks.size() - job.completedColumns,
                        e
                );

                if (!player.isRemoved()) {
                    player.displayClientMessage(
                            Component.literal(
                                    "镜世界映射失败，请检查服务端日志"
                            ),
                            true
                    );
                }

                processedAny = true;
                continue;
            }

            processedAny = true;

            // 任务完成
            if (!job.hasNext()) {
                finishGeneration(playerId, job);
            } else {
                // 当前玩家本轮执行完一列，放到队尾。
                GENERATION_QUEUE.addLast(playerId);
            }
        }
    }

    /**
     * 正常完成一个任务。
     */
    private static void finishGeneration(UUID playerId, MirrorGenerationJob job) {
        GENERATION_JOBS.remove(playerId);

        int clearedDirtyChunks = clearDirtySnapshots(job);

        int retainedDirtyChunks =
                job.dirtySnapshots.size() - clearedDirtyChunks;

        long elapsed =
                System.nanoTime() - job.startedNanos;

        if (Config.debug()) {
            RideBattleParallelWorlds.LOGGER.debug(
                    "Mirror mapping complete: player={}, tasks={}, completed={}, full={}, dirty={}, dirtyChunksCleared={}, dirtyChunksRetained={}, elapsedMs={}",
                    job.player.getName().getString(),
                    job.tasks.size(),
                    job.completedColumns,
                    job.completedFullColumns,
                    job.completedDirtyColumns,
                    clearedDirtyChunks,
                    retainedDirtyChunks,
                    elapsed / 1_000_000.0
            );
        }

        completeMirrorEntry(job);
    }

    /**
     * 异常、玩家离开主世界等情况下取消任务。
     */
    private static void abortGeneration(UUID playerId, MirrorGenerationJob job) {
        GENERATION_JOBS.remove(playerId);

        GENERATION_QUEUE.removeIf(playerId::equals);

        MirrorActiveRegionManager.releaseReservation(playerId);
    }

    /**
     * 玩家离线或外部流程要求取消生成时调用。
     */
    public static void cancelGeneration(ServerPlayer player) {
        UUID playerId = player.getUUID();

        MirrorGenerationJob job =
                GENERATION_JOBS.get(playerId);

        if (job != null) {
            abortGeneration(playerId, job);
        } else {
            GENERATION_QUEUE.removeIf(playerId::equals);
            MirrorActiveRegionManager.releaseReservation(playerId);
        }
    }


    // 映射完成后的传送
    private static void completeMirrorEntry(MirrorGenerationJob job) {
        ServerPlayer player = job.player;

        if (player.isRemoved()
                || player.serverLevel() != job.main) {

            MirrorActiveRegionManager.releaseReservation(
                    player.getUUID()
            );

            return;
        }

        // 实体连续坐标使用 1-X。
        Vec3 mirrorPosition =
                MirrorReflectionTransform.mirrorPosition(player.position());

        player.teleportTo(
                job.mirror,
                mirrorPosition.x,
                player.getY(),
                mirrorPosition.z,
                mirrorYRot(player),
                player.getXRot()
        );

        // 确认玩家确实进入镜世界。
        if (player.serverLevel() != job.mirror) {
            MirrorActiveRegionManager.releaseReservation(
                    player.getUUID()
            );

            RideBattleParallelWorlds.LOGGER.error(
                    "Mirror entry teleport failed: player={}, entrance={}",
                    player.getName().getString(),
                    job.entrance
            );

            return;
        }

        // 将生成预留转换成正式活跃区域引用。
        MirrorActiveRegionManager.activatePlayerRegion(
                player,
                job.regionChunks
        );

        // 镜世界腐蚀计时。
        player.addEffect(new MobEffectInstance(
                ModEffects.MIRROR_CORROSION,
                MirrorConstants.CORROSION_DURATION_SECONDS * 20,
                0,
                false,
                false,
                true
        ));

        // 更新 YBottom 标签，避免重复旧值。
        clearYBottom(player);
        player.addTag(TAG_YBOTTOM + job.yBottom);

        // 初始化最后有效映射位置。
        BlockPos mappedPosition = player.blockPosition();

        if (isMirrorColumnMapped(
                job.main,
                mappedPosition.getX(),
                mappedPosition.getZ()
        )) {
            rememberMappedPosition(player);
        } else if (Config.debug()) {
            RideBattleParallelWorlds.LOGGER.warn(
                    "Mirror boundary initialization failed: player={}, position={}, sourceColumnMapped=false",
                    player.getName().getString(),
                    mappedPosition
            );
        }

        // 采样入口处主世界草地颜色。
        int grassColor = sampleGrassColor(
                job.main,
                job.entrance
        );

        PacketDistributor.sendToPlayer(
                player,
                new MirrorGrassColorSyncPacket(grassColor)
        );
    }

    private static int sampleGrassColor(
            ServerLevel main,
            BlockPos entrance
    ) {
        var biome = main.getBiome(entrance);

        int color = biome.value().getGrassColor(
                entrance.getX(),
                entrance.getZ()
        );

        if (color == -1 || (color & 0xFFFFFF) == 0) {
            return DEFAULT_GRASS_COLOR;
        }

        return color;
    }

    // 退出镜世界
    public static void exitMirror(ServerPlayer player) {
        // 防止外部调用退出时遗留未完成任务或生成预留。
        cancelGeneration(player);

        int yBottom = getYBottom(player);
        ServerLevel main = player.server.overworld();

        if (yBottom == Integer.MIN_VALUE) {
            teleportToSpawn(player, main);
            return;
        }

        Vec3 mainPosition =
                MirrorReflectionTransform.mirrorPosition(
                        player.position()
                );

        player.teleportTo(
                main,
                mainPosition.x,
                player.getY(),
                mainPosition.z,
                mirrorYRot(player),
                player.getXRot()
        );

        cleanupAfterExit(
                player,
                "normal-exit"
        );
    }

    private static void teleportToSpawn(
            ServerPlayer player,
            ServerLevel main
    ) {
        BlockPos spawn = main.getSharedSpawnPos();

        player.teleportTo(
                main,
                spawn.getX() + 0.5D,
                spawn.getY(),
                spawn.getZ() + 0.5D,
                player.getYRot(),
                player.getXRot()
        );

        cleanupAfterExit(
                player,
                "fallback-spawn"
        );
    }

    private static void cleanupAfterExit(
            ServerPlayer player,
            String reason
    ) {
        MirrorActiveRegionManager.releasePlayerRegion(
                player,
                true,
                reason
        );

        player.removeEffect(ModEffects.MIRROR_CORROSION);

        clearYBottom(player);
        clearBoundary(player);
    }

    private static float mirrorYRot(ServerPlayer player) {
        float yRot = player.getYRot();

        Direction direction = player.getDirection();

        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return yRot + 180;
        }

        return yRot;
    }

    // 生成任务对象
    private static final class MirrorGenerationJob {

        private final ServerPlayer player;

        private final ServerLevel main;
        private final ServerLevel mirror;

        private final BlockPos entrance;

        private final int yBottom;
        private final int upperLimit;

        private final List<ColumnTask> tasks;

        /**
         * 本次成功进入后需要保持活跃的 Source Chunk。
         */
        private final Set<Long> regionChunks;

        /**
         * Source Chunk -> 本次任务开始时的 Dirty revision。
         */
        private final Map<LevelChunk, Long> dirtySnapshots;

        private final int requestedColumns;
        private final int newColumns;
        private final int dirtyColumns;

        private final long startedNanos;

        private int nextTask;
        private int completedColumns;
        private int completedFullColumns;
        private int completedDirtyColumns;

        private MirrorGenerationJob(
                ServerPlayer player,
                ServerLevel main,
                ServerLevel mirror,
                BlockPos entrance,
                int yBottom,
                int upperLimit,
                MirrorGenerationPlan plan
        ) {
            this.player = player;

            this.main = main;
            this.mirror = mirror;

            this.entrance = entrance.immutable();

            this.yBottom = yBottom;
            this.upperLimit = upperLimit;

            this.tasks = plan.tasks();
            this.regionChunks = plan.regionChunks();
            this.dirtySnapshots = plan.dirtySnapshots();

            this.requestedColumns = plan.requestedColumns();
            this.newColumns = plan.newColumns();
            this.dirtyColumns = plan.dirtyColumns();

            this.startedNanos = System.nanoTime();
        }

        private boolean hasNext() {
            return nextTask < tasks.size();
        }

        private ColumnTask currentTask() {
            return tasks.get(nextTask);
        }

        private void advance() {
            nextTask++;
        }

        private void recordCompleted(ColumnTask task) {
            completedColumns++;

            if (task.fullScan()) {
                completedFullColumns++;
            } else {
                completedDirtyColumns++;
            }
        }

        public int getRequestedColumns() {
            return requestedColumns;
        }

        public int getNewColumns() {
            return newColumns;
        }

        public int getDirtyColumns() {
            return dirtyColumns;
        }
    }

    // 列扫描
    // 列扫描
    private static void scanColumn(
            ServerLevel source,
            ServerLevel destination,
            int sourceX,
            int z,
            int yBottom,
            int upperLimit
    ) {
        int mirrorX =
                MirrorReflectionTransform.mirrorBlockX(sourceX);

        int sourceTop = findHighestNonAir(
                source,
                sourceX,
                z,
                upperLimit
        );

        int mirrorTop = findHighestNonAir(
                destination,
                mirrorX,
                z,
                upperLimit
        );

        // 同时考虑主世界和镜世界现有高度。
        // 主世界建筑降低后，也能清理镜世界残留结构。
        int upperBound = Math.max(sourceTop, mirrorTop);

        // 两边完全空白时，也必须生成底部玻璃。
        if (upperBound >= yBottom + 1) {
            for (int y = upperBound; y > yBottom; y--) {
                BlockPos sourcePos = new BlockPos(
                        sourceX,
                        y,
                        z
                );

                BlockPos destinationPos = new BlockPos(
                        mirrorX,
                        y,
                        z
                );

                BlockState sourceState =
                        source.getBlockState(sourcePos);

                BlockState currentMirrorState =
                        destination.getBlockState(destinationPos);

                BlockState desiredState;

                if (sourceState.isAir()
                        || !isExposed(source, sourceX, y, z)) {
                    desiredState = Blocks.AIR.defaultBlockState();
                } else {
                    desiredState = mirrorBlockState(sourceState);
                }

                if (!currentMirrorState.equals(desiredState)) {
                    // 同步客户端并跳过形状更新。
                    destination.setBlock(
                            destinationPos,
                            desiredState,
                            3 | 16
                    );
                }
            }
        }

        // YBottom 是镜世界的永久玻璃地板。
        // 不再读取或映射主世界对应位置。
        ensureBounceFloor(
                destination,
                mirrorX,
                z,
                yBottom
        );
    }

    /**
     * 获取最高非空气方块。
     * <p>
     * WORLD_SURFACE Heightmap 避免每列从世界顶部逐层搜索。
     */
    private static int findHighestNonAir(
            ServerLevel level,
            int x,
            int z,
            int upperLimit
    ) {
        int minY = level.getMinBuildHeight();

        int topY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE,
                x,
                z
        ) - 1;

        int boundedTop = Math.min(
                topY,
                Math.min(
                        upperLimit,
                        level.getMaxBuildHeight() - 1
                )
        );

        return boundedTop < minY ? -1 : boundedTop;
    }

    /**
     * 暴露判断。
     * <p>
     * 与原 MirrorScanner 的邻接遮挡规则一致。
     */
    private static boolean isExposed(
            ServerLevel level,
            int x,
            int y,
            int z
    ) {
        BlockPos pos = new BlockPos(x, y, z);

        return !isFullOccluder(level, pos.above())
                || !isFullOccluder(level, pos.below())
                || !isFullOccluder(level, pos.east())
                || !isFullOccluder(level, pos.west())
                || !isFullOccluder(level, pos.south())
                || !isFullOccluder(level, pos.north());
    }

    private static boolean isFullOccluder(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        return state.canOcclude() && state.isCollisionShapeFullBlock(level, pos);
    }

    /**
     * 镜像方块状态
     */
    private static BlockState mirrorBlockState(BlockState original) {
        BlockState result = original;

        // EAST / WEST BooleanProperty
        if (original.hasProperty(BlockStateProperties.EAST)
                && original.hasProperty(BlockStateProperties.WEST)) {

            boolean east =
                    original.getValue(BlockStateProperties.EAST);

            boolean west =
                    original.getValue(BlockStateProperties.WEST);

            if (east != west) {
                result = result
                        .setValue(BlockStateProperties.EAST, west)
                        .setValue(BlockStateProperties.WEST, east);
            }
        }

        // DirectionProperty
        for (Property<?> property : original.getProperties()) {
            if (!(property instanceof DirectionProperty directionProperty)) {
                continue;
            }

            Direction direction =
                    original.getValue(directionProperty);

            Direction mirrored =
                    MirrorReflectionTransform.mirrorDirection(direction);

            if (mirrored != direction) {
                result = result.setValue(
                        directionProperty,
                        mirrored
                );
            }
        }

        // DoorHingeSide
        if (original.hasProperty(DoorBlock.HINGE)) {
            DoorHingeSide hinge =
                    original.getValue(DoorBlock.HINGE);

            DoorHingeSide mirrored =
                    hinge == DoorHingeSide.LEFT
                            ? DoorHingeSide.RIGHT
                            : DoorHingeSide.LEFT;

            result = result.setValue(
                    DoorBlock.HINGE,
                    mirrored
            );
        }

        // StairsShape
        if (original.hasProperty(BlockStateProperties.STAIRS_SHAPE)) {
            StairsShape shape =
                    original.getValue(BlockStateProperties.STAIRS_SHAPE);

            StairsShape mirrored = switch (shape) {
                case INNER_LEFT -> StairsShape.INNER_RIGHT;
                case INNER_RIGHT -> StairsShape.INNER_LEFT;
                case OUTER_LEFT -> StairsShape.OUTER_RIGHT;
                case OUTER_RIGHT -> StairsShape.OUTER_LEFT;
                case STRAIGHT -> shape;
            };

            if (mirrored != shape) {
                result = result.setValue(
                        BlockStateProperties.STAIRS_SHAPE,
                        mirrored
                );
            }
        }

        // RailShape
        if (original.hasProperty(BlockStateProperties.RAIL_SHAPE)) {
            RailShape rail =
                    original.getValue(BlockStateProperties.RAIL_SHAPE);

            RailShape mirrored = mirrorRailShape(rail);

            if (mirrored != rail) {
                result = result.setValue(
                        BlockStateProperties.RAIL_SHAPE,
                        mirrored
                );
            }
        }

        if (original.hasProperty(BlockStateProperties.RAIL_SHAPE_STRAIGHT)) {
            RailShape rail =
                    original.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);

            RailShape mirrored = mirrorRailShape(rail);

            if (mirrored != rail) {
                result = result.setValue(
                        BlockStateProperties.RAIL_SHAPE_STRAIGHT,
                        mirrored
                );
            }
        }

        // 6. Rotation16。
        if (original.hasProperty(BlockStateProperties.ROTATION_16)) {
            int rotation =
                    original.getValue(BlockStateProperties.ROTATION_16);

            if (rotation != 0 && rotation != 8) {
                result = result.setValue(
                        BlockStateProperties.ROTATION_16,
                        16 - rotation
                );
            }
        }

        return result;
    }

    private static RailShape mirrorRailShape(RailShape rail) {
        return switch (rail) {
            case ASCENDING_EAST -> RailShape.ASCENDING_WEST;
            case ASCENDING_WEST -> RailShape.ASCENDING_EAST;

            case SOUTH_EAST -> RailShape.SOUTH_WEST;
            case SOUTH_WEST -> RailShape.SOUTH_EAST;

            case NORTH_EAST -> RailShape.NORTH_WEST;
            case NORTH_WEST -> RailShape.NORTH_EAST;

            default -> rail;
        };
    }

    /**
     * YBottom 处只有空气时才铺玻璃。
     */
    private static void ensureBounceFloor(ServerLevel destination, int mirrorX, int z, int yBottom) {
        BlockPos pos = new BlockPos(
                mirrorX,
                yBottom,
                z
        );

        BlockState glass = Blocks.GLASS.defaultBlockState();

        if (!destination.getBlockState(pos).equals(glass)) {
            destination.setBlock(
                    pos,
                    Blocks.GLASS.defaultBlockState(),
                    3 | 16
            );
        }
    }

    // 玩家 YBottom 标签
    public static int getYBottom(ServerPlayer player) {
        for (String tag : player.getTags()) {
            if (!tag.startsWith(TAG_YBOTTOM)) {
                continue;
            }

            try {
                return Integer.parseInt(
                        tag.substring(TAG_YBOTTOM.length())
                );
            } catch (NumberFormatException ignored) {
                // 忽略损坏的旧标签。
            }
        }

        return Integer.MIN_VALUE;
    }

    public static void clearYBottom(ServerPlayer player) {
        List<String> toRemove = new ArrayList<>();

        for (String tag : player.getTags()) {
            if (tag.startsWith(TAG_YBOTTOM)) {
                toRemove.add(tag);
            }
        }

        for (String tag : toRemove) {
            player.removeTag(tag);
        }
    }
}