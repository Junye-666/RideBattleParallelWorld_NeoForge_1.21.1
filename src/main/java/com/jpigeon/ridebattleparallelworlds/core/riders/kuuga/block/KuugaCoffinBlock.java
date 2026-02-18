package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.block;


import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.BaseKamenRiderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class KuugaCoffinBlock extends BaseKamenRiderBlock {
    public KuugaCoffinBlock(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super("kuuga", "kuuga_coffin", type, pos, blockState, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {

    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return null;
    }
}
