package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.block;


import com.jpigeon.ridebattleparallelworlds.core.block.ModBlockEntities;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.BaseRiderGeoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimatableManager;

public class KuugaCoffinBlockEntity extends BaseRiderGeoBlockEntity {
    public KuugaCoffinBlockEntity(BlockPos pos, BlockState blockState) {
        super("kuuga", "kuuga_coffin", ModBlockEntities.KUUGA_COFFIN_BE.get(), pos, blockState, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }
}
