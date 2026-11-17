package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 单个主世界源区块的镜世界覆盖数据。
 * <p>
 * 一个 bit 对应一个 X/Z 方块列。
 * 16 * 16 = 256 列，使用 4 个 long 保存。
 * <p>
 * 本类同时负责覆盖数据的查询和标记。
 * <p>
 * 所有世界级方法必须在服务端主线程调用。
 */
public final class MirrorCoverageData {

    // 保留项目当前使用的映射版本号，不需要因为 YBottom 再次升级。
    public static final int CURRENT_MAPPING_VERSION = 1;

    private static final int BIT_COUNT = 16 * 16;
    private static final int LONG_COUNT = BIT_COUNT / Long.SIZE;

    /**
     * 尚未记录过有效 YBottom 时使用。
     * <p>
     * 不可能与正常配置值冲突。
     */
    private static final int UNKNOWN_Y_BOTTOM = Integer.MIN_VALUE;

    private int mappingVersion;

    /**
     * 上一次完成映射时使用的 YBottom。
     */
    private int mappedYBottom;

    private final long[] mappedColumns;

    public MirrorCoverageData() {
        this(
                CURRENT_MAPPING_VERSION,
                List.of(),
                UNKNOWN_Y_BOTTOM
        );
    }

    private MirrorCoverageData(
            int mappingVersion,
            List<Long> serializedBits,
            int mappedYBottom
    ) {
        this.mappingVersion = mappingVersion;
        this.mappedYBottom = mappedYBottom;
        this.mappedColumns = new long[LONG_COUNT];

        // 旧版本覆盖数据不能直接继承。
        if (mappingVersion != CURRENT_MAPPING_VERSION
                || serializedBits == null) {
            return;
        }

        int limit = Math.min(
                serializedBits.size(),
                LONG_COUNT
        );

        for (int i = 0; i < limit; i++) {
            this.mappedColumns[i] = serializedBits.get(i);
        }
    }

    /**
     * Attachment 序列化。
     * <p>
     * mapped_y_bottom 使用可选字段，
     * 兼容没有该字段的旧存档。
     * 旧存档会因为 UNKNOWN_Y_BOTTOM 自动触发重新映射。
     */
    public static final Codec<MirrorCoverageData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT
                                    .optionalFieldOf(
                                            "mapping_version",
                                            CURRENT_MAPPING_VERSION
                                    )
                                    .forGetter(data -> data.mappingVersion),

                            Codec.LONG.listOf()
                                    .optionalFieldOf(
                                            "mapped_columns",
                                            List.of()
                                    )
                                    .forGetter(MirrorCoverageData::serializedBits),

                            Codec.INT
                                    .optionalFieldOf(
                                            "mapped_y_bottom",
                                            UNKNOWN_Y_BOTTOM
                                    )
                                    .forGetter(data -> data.mappedYBottom)

                    ).apply(instance, MirrorCoverageData::new)
            );

    // =========================================================
    // 数据本身
    // =========================================================

    /**
     * 查询列是否已经完成当前配置下的映射。
     */
    public boolean isColumnMapped(int localX, int localZ) {
        checkLocalCoordinates(localX, localZ);

        // 映射算法版本或 YBottom 不一致时，旧覆盖记录失效。
        if (mappingVersion != CURRENT_MAPPING_VERSION
                || mappedYBottom != MirrorConstants.yBottom()) {
            return false;
        }

        int bitIndex = (localZ << 4) | localX;
        int wordIndex = bitIndex >>> 6;
        long mask = 1L << (bitIndex & 63);

        return (mappedColumns[wordIndex] & mask) != 0L;
    }

    /**
     * 标记列已经完成完整映射。
     *
     * @param localX  区块内 X 坐标
     * @param localZ  区块内 Z 坐标
     * @param yBottom 本次扫描实际使用的 YBottom
     * @return 是否实际改变了数据
     */
    public boolean markColumnMapped(
            int localX,
            int localZ,
            int yBottom
    ) {
        checkLocalCoordinates(localX, localZ);

        ensureCurrentVersion();
        ensureCurrentYBottom(yBottom);

        int bitIndex = (localZ << 4) | localX;
        int wordIndex = bitIndex >>> 6;
        long mask = 1L << (bitIndex & 63);

        if ((mappedColumns[wordIndex] & mask) != 0L) {
            return false;
        }

        mappedColumns[wordIndex] |= mask;
        return true;
    }

    public boolean isFullyMapped() {
        if (mappingVersion != CURRENT_MAPPING_VERSION
                || mappedYBottom != MirrorConstants.yBottom()) {
            return false;
        }

        for (long word : mappedColumns) {
            if (word != -1L) {
                return false;
            }
        }

        return true;
    }

    public int getMappingVersion() {
        return mappingVersion;
    }

    /**
     * 映射算法版本变化时清空旧覆盖数据。
     */
    private void ensureCurrentVersion() {
        if (mappingVersion == CURRENT_MAPPING_VERSION) {
            return;
        }

        clearMappedColumns();

        mappingVersion = CURRENT_MAPPING_VERSION;
        mappedYBottom = UNKNOWN_Y_BOTTOM;
    }

    /**
     * YBottom 改变时清空整个源区块的覆盖记录。
     * <p>
     * 因为一个 Attachment 对应整个 16x16 区块，
     * 所以该区块内的 256 列统一使用同一个 YBottom 标记。
     */
    private void ensureCurrentYBottom(int yBottom) {
        if (mappedYBottom == yBottom) {
            return;
        }

        clearMappedColumns();

        mappedYBottom = yBottom;
    }

    private void clearMappedColumns() {
        Arrays.fill(mappedColumns, 0L);
    }

    private List<Long> serializedBits() {
        List<Long> result = new ArrayList<>(LONG_COUNT);

        for (long word : mappedColumns) {
            result.add(word);
        }

        return result;
    }

    private static void checkLocalCoordinates(
            int localX,
            int localZ
    ) {
        if (localX < 0 || localX > 15
                || localZ < 0 || localZ > 15) {
            throw new IllegalArgumentException(
                    "Chunk-local X/Z must be in 0..15: "
                            + localX + ", " + localZ
            );
        }
    }

    // =========================================================
    // 世界级覆盖接口
    // =========================================================

    /**
     * 查询主世界指定 X/Z 列是否已经完成映射。
     */
    public static boolean isColumnMapped(
            ServerLevel sourceLevel,
            int sourceX,
            int sourceZ
    ) {
        LevelChunk sourceChunk = sourceLevel.getChunk(
                sourceX >> 4,
                sourceZ >> 4
        );

        var attachmentType = PWAttachments.MIRROR_COVERAGE.get();

        if (!sourceChunk.hasData(attachmentType)) {
            return false;
        }

        MirrorCoverageData data =
                sourceChunk.getData(attachmentType);

        return data.isColumnMapped(
                sourceX & 15,
                sourceZ & 15
        );
    }

    /**
     * 标记主世界指定列已经完成完整映射。
     * <p>
     * 必须在整列扫描成功后调用。
     *
     * @param yBottom 本次生成任务实际使用的 YBottom
     */
    public static void markColumnMapped(
            ServerLevel sourceLevel,
            int sourceX,
            int sourceZ,
            int yBottom
    ) {
        LevelChunk sourceChunk = sourceLevel.getChunk(
                sourceX >> 4,
                sourceZ >> 4
        );

        var attachmentType = PWAttachments.MIRROR_COVERAGE.get();

        MirrorCoverageData data =
                sourceChunk.getData(attachmentType);

        boolean changed = data.markColumnMapped(
                sourceX & 15,
                sourceZ & 15,
                yBottom
        );

        if (changed) {
            // Attachment 对象被原地修改，必须显式标记区块待保存。
            sourceChunk.setUnsaved(true);
        }
    }
}