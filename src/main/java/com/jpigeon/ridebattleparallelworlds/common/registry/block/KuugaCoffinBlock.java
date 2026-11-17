package com.jpigeon.ridebattleparallelworlds.common.registry.block;


import com.jpigeon.ridebattleparallelworlds.common.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KuugaCoffinBlock extends BaseEntityBlock{
    public static final VoxelShape SHAPE = Block.box(0, 0, -8, 16, 8, 24);
    public static final MapCodec<KuugaCoffinBlock> CODEC = simpleCodec(KuugaCoffinBlock::new);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public KuugaCoffinBlock(Properties properties) {
        super(properties.noTerrainParticles().destroyTime(5).explosionResistance(5).sound(SoundType.DEEPSLATE));

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(OPEN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(OPEN);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new KuugaCoffinBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof KuugaCoffinBlockEntity)) {
            return super.useWithoutItem(
                    state,
                    level,
                    pos,
                    player,
                    hitResult
            );
        }

        // 客户端只响应交互，不修改实际状态
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (state.getValue(OPEN)) {
            // 已打开：第二次点击摧毁棺材
            level.destroyBlock(pos, false, player, 1);
        } else {
            // 未打开：切换 BlockState
            level.setBlock(
                    pos,
                    state.setValue(OPEN, true),
                    Block.UPDATE_ALL
            );

            level.updateNeighbourForOutputSignal(pos, this);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (movedByPiston) return;
        if (!level.isClientSide()
                && state.getBlock() != newState.getBlock()) {
            level.addFreshEntity(
                    new ItemEntity(
                            level,
                            pos.getX(), pos.getY(), pos.getZ(),
                            ModItems.Kuuga.ARCLE.toStack()
                    )
            );

            level.updateNeighbourForOutputSignal(pos, this);
        }
    }
}
