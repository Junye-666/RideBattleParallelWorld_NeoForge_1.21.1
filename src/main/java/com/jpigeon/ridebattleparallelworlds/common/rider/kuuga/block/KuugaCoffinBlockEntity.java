package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.block;


import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModBlockEntities;
import com.jpigeon.rideevolutionlib.compat.geckoLib.block.BaseRiderGeoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimatableManager;

public class KuugaCoffinBlockEntity extends BaseRiderGeoBlockEntity {
    public KuugaCoffinBlockEntity(BlockPos pos, BlockState blockState) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "kuuga_coffin", ModBlockEntities.KUUGA_COFFIN_BE.get(), pos, blockState, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }

    public boolean isOpen() {
        return !getCurrentAnimState().equals("idle");
    }
}
