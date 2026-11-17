package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/**
 * 镜世界统一坐标变换。
 * <p>
 * 方块坐标：
 * mirrorX = -sourceX
 * <p>
 * 实体连续坐标：
 * mirrorX = 1 - sourceX
 * <p>
 * 镜像平面为 X = 0.5。
 */
public final class MirrorReflectionTransform {

    private MirrorReflectionTransform() {
    }

    /**
     * 方块 X 坐标镜像。
     */
    public static int mirrorBlockX(int sourceX) {
        return -sourceX;
    }

    /**
     * 实体连续 X 坐标镜像。
     */
    public static double mirrorEntityX(double sourceX) {
        return 1.0D - sourceX;
    }

    /**
     * 方块坐标镜像。
     */
    public static BlockPos mirrorBlockPos(BlockPos sourcePos) {
        return new BlockPos(
                mirrorBlockX(sourcePos.getX()),
                sourcePos.getY(),
                sourcePos.getZ()
        );
    }

    /**
     * 实体位置镜像。
     */
    public static Vec3 mirrorPosition(Vec3 sourcePosition) {
        return new Vec3(
                mirrorEntityX(sourcePosition.x),
                sourcePosition.y,
                sourcePosition.z
        );
    }

    /**
     * Direction 镜像。
     */
    public static Direction mirrorDirection(Direction sourceDirection) {
        return switch (sourceDirection) {
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
            default -> sourceDirection;
        };
    }
}