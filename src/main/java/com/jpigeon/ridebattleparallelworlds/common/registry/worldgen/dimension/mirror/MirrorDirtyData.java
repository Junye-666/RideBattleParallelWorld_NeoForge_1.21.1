package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单个主世界源 Chunk 的镜像 Dirty 数据。
 * <p>
 * 只记录已经映射区域内发生变化的方块位置。
 * <p>
 * Dirty 记录的是位置，不是方块变化历史。
 * <p>
 * 当 Dirty 数量超过上限时，升级为完整 Chunk 重扫。
 * <p>
 * 本类同时负责主世界方块修改追踪。
 */
public final class MirrorDirtyData {

    public static final int MAX_DIRTY_BLOCKS = 4096;

    private final Set<Long> dirtyPositions = new HashSet<>();

    private boolean needsFullRescan;
    private long revision;

    public MirrorDirtyData() {
        this(false, List.of(), 0L);
    }

    private MirrorDirtyData(
            boolean needsFullRescan,
            List<Long> serializedPositions,
            long revision
    ) {
        this.needsFullRescan = needsFullRescan;
        this.revision = Math.max(0L, revision);

        if (needsFullRescan || serializedPositions == null) {
            return;
        }

        for (long packedPos : serializedPositions) {
            dirtyPositions.add(packedPos);

            if (dirtyPositions.size() > MAX_DIRTY_BLOCKS) {
                this.needsFullRescan = true;
                dirtyPositions.clear();
                break;
            }
        }
    }

    /**
     * Attachment 序列化字段保持旧格式，兼容已有存档。
     */
    public static final Codec<MirrorDirtyData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.BOOL
                                    .optionalFieldOf(
                                            "needs_full_rescan",
                                            false
                                    )
                                    .forGetter(data -> data.needsFullRescan),

                            Codec.LONG.listOf()
                                    .optionalFieldOf(
                                            "dirty_positions",
                                            List.of()
                                    )
                                    .forGetter(MirrorDirtyData::serializedPositions),

                            Codec.LONG
                                    .optionalFieldOf(
                                            "revision",
                                            0L
                                    )
                                    .forGetter(data -> data.revision)

                    ).apply(instance, MirrorDirtyData::new)
            );

    // =========================================================
    // Dirty 数据操作
    // =========================================================

    /**
     * 记录一个发生变化的方块位置。
     * <p>
     * 同一位置重复修改时，revision 仍然递增。
     */
    public boolean markDirty(BlockPos pos) {
        revision++;

        if (needsFullRescan) {
            return true;
        }

        dirtyPositions.add(pos.asLong());

        if (dirtyPositions.size() > MAX_DIRTY_BLOCKS) {
            needsFullRescan = true;
            dirtyPositions.clear();
        }

        return true;
    }

    public boolean needsFullRescan() {
        return needsFullRescan;
    }

    public int getDirtyCount() {
        return dirtyPositions.size();
    }

    public long getRevision() {
        return revision;
    }

    /**
     * 返回独立快照。
     * <p>
     * 调用方应当每个 Dirty Chunk 只获取一次快照，
     * 并在本轮计划构建中复用。
     */
    public Set<BlockPos> getDirtyPositions() {
        Set<BlockPos> result = new HashSet<>(dirtyPositions.size());

        for (long packedPos : dirtyPositions) {
            result.add(BlockPos.of(packedPos));
        }

        return Set.copyOf(result);
    }

    /**
     * 只有提交时 revision 与快照完全一致才允许调用。
     */
    public void clearAfterSync() {
        dirtyPositions.clear();
        needsFullRescan = false;
        revision++;
    }

    public void markFullRescan() {
        revision++;
        needsFullRescan = true;
        dirtyPositions.clear();
    }

    private List<Long> serializedPositions() {
        List<Long> result = new ArrayList<>(dirtyPositions);
        result.sort(Long::compare);
        return result;
    }

    // =========================================================
    // 主世界方块修改追踪
    // =========================================================

    /**
     * 在主世界方块状态真正发生变化后调用。
     * <p>
     * 只追踪会影响已经存在镜像列的修改。
     * <p>
     * 当前检查：
     * 自身、东、西、南、北五个水平列。
     */
    public static void markBlockChanged(
            LevelChunk sourceChunk,
            BlockPos pos,
            BlockState oldState,
            BlockState newState
    ) {
        if (oldState == null || oldState.equals(newState)) {
            return;
        }

        if (!(sourceChunk.getLevel() instanceof ServerLevel sourceLevel)) {
            return;
        }

        // 镜像只由主世界驱动。
        if (!sourceLevel.dimension().equals(Level.OVERWORLD)) {
            return;
        }

        int x = pos.getX();
        int z = pos.getZ();

        int affectedMappedColumns = 0;

        if (isColumnMapped(sourceLevel, sourceChunk, x, z)) {
            affectedMappedColumns++;
        }

        if (isColumnMapped(sourceLevel, sourceChunk, x + 1, z)) {
            affectedMappedColumns++;
        }

        if (isColumnMapped(sourceLevel, sourceChunk, x - 1, z)) {
            affectedMappedColumns++;
        }

        if (isColumnMapped(sourceLevel, sourceChunk, x, z + 1)) {
            affectedMappedColumns++;
        }

        if (isColumnMapped(sourceLevel, sourceChunk, x, z - 1)) {
            affectedMappedColumns++;
        }

        if (affectedMappedColumns == 0) {
            return;
        }

        var attachmentType = PWAttachments.MIRROR_DIRTY.get();

        MirrorDirtyData dirty =
                sourceChunk.getData(attachmentType);

        dirty.markDirty(pos);

        // Attachment 原地修改后，必须标记区块待保存。
        sourceChunk.setUnsaved(true);

        // 默认不逐方块刷 INFO，避免 WorldEdit 等操作产生大量日志。
        if (Config.debug()) {
            RideBattleParallelWorlds.LOGGER.debug(
                    "Mirror Dirty recorded: pos={}, old={}, new={}, sourceChunk={}, affectedMappedColumns={}, revision={}",
                    pos,
                    oldState,
                    newState,
                    sourceChunk.getPos(),
                    affectedMappedColumns,
                    dirty.getRevision()
            );
        }
    }

    /**
     * 查询一个源世界列是否已经映射。
     * <p>
     * 同 Chunk 直接读取原 Chunk，只有跨 Chunk 边界时才获取相邻 Chunk。
     */
    private static boolean isColumnMapped(
            ServerLevel sourceLevel,
            LevelChunk originalChunk,
            int x,
            int z
    ) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        LevelChunk targetChunk;

        if (originalChunk.getPos().x == chunkX
                && originalChunk.getPos().z == chunkZ) {
            targetChunk = originalChunk;
        } else {
            targetChunk = sourceLevel.getChunk(chunkX, chunkZ);
        }

        var coverageType = PWAttachments.MIRROR_COVERAGE.get();

        if (!targetChunk.hasData(coverageType)) {
            return false;
        }

        MirrorCoverageData coverage =
                targetChunk.getData(coverageType);

        return coverage.isColumnMapped(
                x & 15,
                z & 15
        );
    }
}