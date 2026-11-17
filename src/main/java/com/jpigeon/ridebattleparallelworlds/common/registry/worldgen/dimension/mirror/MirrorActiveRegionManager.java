package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public final class MirrorActiveRegionManager {

    private MirrorActiveRegionManager() {
    }

    /**
     * 玩家持久化 Tag。
     * <p>
     * 每个 Tag 记录一个主世界 Source Chunk。
     * <p>
     * 例如：
     * rbpw_mirror_active_chunk:4294967295
     */
    private static final String TAG_ACTIVE_CHUNK =
            "rbpw_mirror_active_chunk:";

    /**
     * Source Chunk -> 当前活跃玩家数量。
     */
    private static final Map<Long, Integer> ACTIVE_COUNTS =
            new HashMap<>();

    /**
     * Player UUID -> 当前占用的 Source Chunk 集合。
     */
    private static final Map<UUID, Set<Long>> ACTIVE_BY_PLAYER =
            new HashMap<>();

    /**
     * Source Chunk -> 当前持有生成锁的玩家 UUID。
     */
    private static final Map<Long, UUID> GENERATION_LOCKS =
            new HashMap<>();

    /**
     * 玩家 UUID -> 当前持有的生成锁集合。
     */
    private static final Map<UUID, Set<Long>> RESERVATIONS_BY_PLAYER =
            new HashMap<>();

    // =========================================================
    // Chunk Key
    // =========================================================

    /**
     * 将 Chunk X/Z 编码为一个 long。
     * <p>
     * 这里使用独立编码，不依赖 BlockPos 的坐标编码。
     */
    public static long chunkKey(int chunkX, int chunkZ) {

        return ((long) chunkX & 0xFFFFFFFFL)
                | (((long) chunkZ & 0xFFFFFFFFL) << 32);
    }

    // 活跃状态
    public static boolean isChunkActive(long key) {

        return ACTIVE_COUNTS.getOrDefault(key, 0) > 0;
    }

    public static int getActiveCount(long key) {

        return ACTIVE_COUNTS.getOrDefault(key, 0);
    }

    public static boolean hasRuntimeRegion(UUID playerId) {

        return ACTIVE_BY_PLAYER.containsKey(playerId);
    }

    /**
     * 检查玩家是否存在运行时区域或持久化区域记录。
     */
    public static boolean hasPlayerRegionState(ServerPlayer player) {

        if (ACTIVE_BY_PLAYER.containsKey(player.getUUID())) {
            return true;
        }

        for (String tag : player.getTags()) {

            if (tag.startsWith(TAG_ACTIVE_CHUNK)) {
                return true;
            }
        }

        return false;
    }

    // =========================================================
    // 生成锁
    // =========================================================

    /**
     * 尝试一次性预留一组 Source Chunk。
     * <p>
     * 如果其中任何一个 Chunk 已经被其他生成任务锁定，
     * 本次预留全部失败，不会留下部分锁。
     */
    public static boolean tryReserve(
            UUID playerId,
            Set<Long> requestedChunks
    ) {

        Set<Long> requested = Set.copyOf(requestedChunks);

        // 第一遍：检查所有冲突。
        for (long key : requested) {

            UUID currentOwner = GENERATION_LOCKS.get(key);

            if (currentOwner != null
                    && !currentOwner.equals(playerId)) {

                RideBattleParallelWorlds.LOGGER.warn(
                        "Mirror region reservation blocked: player={}, chunk={}, currentOwner={}",
                        playerId,
                        key,
                        currentOwner
                );

                return false;
            }
        }

        // 检查通过后，才替换旧预留。
        releaseReservationInternal(playerId);

        for (long key : requested) {

            GENERATION_LOCKS.put(key, playerId);
        }

        RESERVATIONS_BY_PLAYER.put(playerId, requested);

        RideBattleParallelWorlds.LOGGER.info(
                "Mirror region reserved: player={}, reservedChunks={}",
                playerId,
                requested.size()
        );

        return true;
    }

    /**
     * 判断是否被其他玩家的生成任务锁定。
     */
    public static boolean isReservedByOther(
            UUID playerId,
            long chunkKey
    ) {

        UUID owner = GENERATION_LOCKS.get(chunkKey);

        return owner != null && !owner.equals(playerId);
    }

    /**
     * 释放玩家生成任务持有的全部锁。
     */
    public static void releaseReservation(UUID playerId) {

        Set<Long> released = releaseReservationInternal(playerId);

        if (!released.isEmpty()) {

            RideBattleParallelWorlds.LOGGER.info(
                    "Mirror region reservation released: player={}, releasedChunks={}",
                    playerId,
                    released.size()
            );
        }
    }

    private static Set<Long> releaseReservationInternal(UUID playerId) {

        Set<Long> reserved = RESERVATIONS_BY_PLAYER.remove(playerId);

        if (reserved == null || reserved.isEmpty()) {
            return Set.of();
        }

        for (long key : reserved) {

            UUID owner = GENERATION_LOCKS.get(key);

            if (playerId.equals(owner)) {

                GENERATION_LOCKS.remove(key);
            }
        }

        return reserved;
    }

    // =========================================================
    // 玩家进入镜世界
    // =========================================================

    /**
     * 玩家成功进入镜世界后调用。
     * <p>
     * 1. 保存 Source Chunk 集合到玩家 Tag。
     * 2. 增加每个 Source Chunk 的活跃引用数。
     * 3. 释放生成锁。
     */
    public static void activatePlayerRegion(
            ServerPlayer player,
            Set<Long> sourceChunks
    ) {

        UUID playerId = player.getUUID();

        Set<Long> next = Set.copyOf(sourceChunks);

        // 如果玩家此前已经有运行时区域，先释放旧引用。
        replaceRuntimeRegion(playerId, next);

        // 持久化本次进入时实际占用的 Source Chunk 集合。
        writePersistentRegion(player, next);

        // 玩家现在已经正式进入，生成锁不再需要。
        releaseReservation(playerId);

        RideBattleParallelWorlds.LOGGER.info(
                "Mirror active region acquired: player={}, chunks={}, activeChunkCount={}",
                player.getName().getString(),
                next.size(),
                ACTIVE_COUNTS.size()
        );
    }

    /**
     * 登录或重生时恢复玩家的活跃区域。
     * <p>
     * 恢复顺序：
     * 1. 玩家持久化 Chunk Tag
     * 2. 当前服务端运行时记录（用于同一进程内重生）
     * <p>
     * 不再从旧 Boundary 半径推算区域。
     */
    public static boolean restorePlayerRegion(ServerPlayer player) {
        UUID playerId = player.getUUID();

        Set<Long> restored = readPersistentRegion(player);

        // 同一服务端进程内重生时，持久化 Tag 可能没有完整复制，
        // 使用原玩家的运行时区域作为后备。
        if (restored.isEmpty()) {
            Set<Long> runtime = ACTIVE_BY_PLAYER.get(playerId);

            if (runtime != null && !runtime.isEmpty()) {
                restored = new HashSet<>(runtime);
            }
        }

        // 没有任何可恢复数据时，不伪造一个完整区域。
        if (restored.isEmpty()) {
            RideBattleParallelWorlds.LOGGER.error(
                    "Mirror active region restore failed: player={}, no persistent or runtime region",
                    player.getName().getString()
            );

            return false;
        }

        // 替换运行时引用，避免重复计数。
        replaceRuntimeRegion(playerId, restored);

        // 确保玩家数据中保留完整区域记录。
        writePersistentRegion(player, restored);

        RideBattleParallelWorlds.LOGGER.info(
                "Mirror active region restored: player={}, chunks={}, activeChunkCount={}",
                player.getName().getString(),
                restored.size(),
                ACTIVE_COUNTS.size()
        );

        return true;
    }

    /**
     * 替换某个玩家的运行时占用集合。
     */
    private static void replaceRuntimeRegion(
            UUID playerId,
            Set<Long> next
    ) {

        Set<Long> previous = ACTIVE_BY_PLAYER.remove(playerId);

        if (previous != null) {

            for (long key : previous) {

                decrementActiveCount(key, playerId);
            }
        }

        if (next.isEmpty()) {
            return;
        }

        Set<Long> snapshot = Set.copyOf(next);

        ACTIVE_BY_PLAYER.put(playerId, snapshot);

        for (long key : snapshot) {

            ACTIVE_COUNTS.merge(key, 1, Integer::sum);
        }
    }

    private static void decrementActiveCount(
            long key,
            UUID playerId
    ) {

        int count = ACTIVE_COUNTS.getOrDefault(key, 0);

        if (count <= 0) {

            ACTIVE_COUNTS.remove(key);

            RideBattleParallelWorlds.LOGGER.error(
                    "Mirror active reference repaired: player={}, chunk={}, invalidCount={}",
                    playerId,
                    key,
                    count
            );

            return;
        }

        if (count == 1) {

            ACTIVE_COUNTS.remove(key);

        } else {

            ACTIVE_COUNTS.put(key, count - 1);
        }
    }

    /**
     * 正常退出、异常离开镜世界时调用。
     * <p>
     * clearPersistent=true：
     * 同时删除玩家持久化区域记录。
     * <p>
     * clearPersistent=false：
     * 只释放运行时引用，保留 Tag。
     * 用于玩家断线后重新登录镜世界。
     */
    public static void releasePlayerRegion(
            ServerPlayer player,
            boolean clearPersistent,
            String reason
    ) {

        UUID playerId = player.getUUID();

        Set<Long> previous =
                ACTIVE_BY_PLAYER.remove(playerId);

        int releasedChunks = 0;

        if (previous != null) {

            releasedChunks = previous.size();

            for (long key : previous) {

                decrementActiveCount(key, playerId);
            }
        }

        releaseReservation(playerId);

        int removedTags = 0;

        if (clearPersistent) {

            removedTags = clearPersistentRegion(player);
        }

        if (releasedChunks > 0
                || removedTags > 0) {

            if (Config.debug()) {
                RideBattleParallelWorlds.LOGGER.info(
                        "Mirror active region released: player={}, reason={}, releasedChunks={}, removedTags={}, remainingActiveChunks={}",
                        player.getName().getString(),
                        reason,
                        releasedChunks,
                        removedTags,
                        ACTIVE_COUNTS.size()
                );
            }
        }
    }

    /**
     * 玩家断线时调用。
     * <p>
     * 释放运行时引用，但保留玩家区域 Tag。
     * 玩家重新登录镜世界时可以恢复。
     */
    public static void releaseForLogout(ServerPlayer player) {

        releasePlayerRegion(
                player,
                false,
                "logout"
        );
    }

    // ========== 持久化 Tag ==========

    private static void writePersistentRegion(
            ServerPlayer player,
            Set<Long> chunks
    ) {

        clearPersistentRegion(player);

        for (long key : chunks) {

            player.addTag(
                    TAG_ACTIVE_CHUNK + key
            );
        }
    }

    public static Set<Long> readPersistentRegion(
            ServerPlayer player
    ) {

        Set<Long> result = new HashSet<>();

        for (String tag : player.getTags()) {

            if (!tag.startsWith(TAG_ACTIVE_CHUNK)) {
                continue;
            }

            String value =
                    tag.substring(TAG_ACTIVE_CHUNK.length());

            try {

                result.add(Long.parseLong(value));

            } catch (NumberFormatException e) {

                RideBattleParallelWorlds.LOGGER.warn(
                        "Invalid Mirror active chunk tag: player={}, tag={}",
                        player.getName().getString(),
                        tag
                );
            }
        }

        return result;
    }

    private static int clearPersistentRegion(
            ServerPlayer player
    ) {

        List<String> toRemove = new ArrayList<>();

        for (String tag : player.getTags()) {

            if (tag.startsWith(TAG_ACTIVE_CHUNK)) {

                toRemove.add(tag);
            }
        }

        for (String tag : toRemove) {

            player.removeTag(tag);
        }

        return toRemove.size();
    }
}