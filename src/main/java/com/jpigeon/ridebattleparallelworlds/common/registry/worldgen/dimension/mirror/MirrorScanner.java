package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

public final class MirrorScanner {
    private MirrorScanner() {
    }

    public static int findHighestNonAir(ServerLevel level, int x, int z, int upperLimit) {
        int minY = level.getMinBuildHeight();
        int startY = Math.min(upperLimit, level.getMaxBuildHeight() - 1);
        for (int y = startY; y >= minY; y--) {
            if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) return y;
        }
        return -1;
    }

    public static boolean isExposed(ServerLevel level, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return !isFullOccluder(level, pos.above())
                || !isFullOccluder(level, pos.below())
                || !isFullOccluder(level, pos.east())
                || !isFullOccluder(level, pos.west())
                || !isFullOccluder(level, pos.south())
                || !isFullOccluder(level, pos.north());
    }

    /** 完整遮挡方块 = 不透明 + 碰撞形状是完整方块 */
    private static boolean isFullOccluder(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canOcclude() && state.isCollisionShapeFullBlock(level, pos);
    }

    public static BlockState mirrorBlockState(BlockState state) {
        BlockState original = state;
        BlockState result = state;

        // 1. 布尔对：EAST / WEST 互换（栅栏、墙、红石线、藤蔓、发光地衣等）
        if (original.hasProperty(BlockStateProperties.EAST)
                && original.hasProperty(BlockStateProperties.WEST)) {
            boolean east = original.getValue(BlockStateProperties.EAST);
            boolean west = original.getValue(BlockStateProperties.WEST);
            if (east != west) {
                result = result.setValue(BlockStateProperties.EAST, west)
                        .setValue(BlockStateProperties.WEST, east);
            }
        }

        // 2. DirectionProperty：EAST↔WEST，其他（NORTH/SOUTH/UP/DOWN）不变
        //    覆盖：楼梯、门、活板门、箱子、熔炉、活塞、漏斗、观察者、告示牌（wall）、
        //         南瓜、紫颂花、命令方块、结构方块、发射器、投掷器……
        for (Property<?> prop : original.getProperties()) {
            if (!(prop instanceof DirectionProperty dirProp)) continue;
            Direction d = original.getValue(dirProp);
            Direction m = mirrorDirection(d);
            if (m != d) {
                result = result.setValue(dirProp, m);
            }
        }

        // 3. DoorHingeSide：LEFT↔RIGHT
        if (original.hasProperty(DoorBlock.HINGE)) {
            DoorHingeSide hinge = original.getValue(DoorBlock.HINGE);
            DoorHingeSide m = hinge == DoorHingeSide.LEFT
                    ? DoorHingeSide.RIGHT
                    : DoorHingeSide.LEFT;
            result = result.setValue(DoorBlock.HINGE, m);
        }

        // 4. StairsShape：INNER_LEFT↔INNER_RIGHT, OUTER_LEFT↔OUTER_RIGHT
        if (original.hasProperty(BlockStateProperties.STAIRS_SHAPE)) {
            StairsShape shape = original.getValue(BlockStateProperties.STAIRS_SHAPE);
            StairsShape m = switch (shape) {
                case INNER_LEFT  -> StairsShape.INNER_RIGHT;
                case INNER_RIGHT -> StairsShape.INNER_LEFT;
                case OUTER_LEFT  -> StairsShape.OUTER_RIGHT;
                case OUTER_RIGHT -> StairsShape.OUTER_LEFT;
                case STRAIGHT    -> shape;
            };
            if (m != shape) {
                result = result.setValue(BlockStateProperties.STAIRS_SHAPE, m);
            }
        }

        // 5. RailShape
        if (original.hasProperty(BlockStateProperties.RAIL_SHAPE)) {
            RailShape rail = original.getValue(BlockStateProperties.RAIL_SHAPE);
            RailShape m = mirrorRailShape(rail);
            if (m != rail) {
                result = result.setValue(BlockStateProperties.RAIL_SHAPE, m);
            }
        }
        if (original.hasProperty(BlockStateProperties.RAIL_SHAPE_STRAIGHT)) {
            RailShape rail = original.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
            RailShape m = mirrorRailShape(rail);
            if (m != rail) {
                result = result.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, m);
            }
        }

        // 6. Rotation（16 向，告示牌/旗帜/头）：南/北不变，西↔东
        if (original.hasProperty(BlockStateProperties.ROTATION_16)) {
            int r = original.getValue(BlockStateProperties.ROTATION_16);
            if (r != 0 && r != 8) {
                result = result.setValue(BlockStateProperties.ROTATION_16, 16 - r);
            }
        }

        return result;
    }

    private static Direction mirrorDirection(Direction d) {
        return switch (d) {
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
            default   -> d;
        };
    }

    private static RailShape mirrorRailShape(RailShape rail) {
        return switch (rail) {
            case ASCENDING_EAST -> RailShape.ASCENDING_WEST;
            case ASCENDING_WEST -> RailShape.ASCENDING_EAST;
            case SOUTH_EAST     -> RailShape.SOUTH_WEST;
            case SOUTH_WEST     -> RailShape.SOUTH_EAST;
            case NORTH_EAST     -> RailShape.NORTH_WEST;
            case NORTH_WEST     -> RailShape.NORTH_EAST;
            default -> rail;
        };
    }
}