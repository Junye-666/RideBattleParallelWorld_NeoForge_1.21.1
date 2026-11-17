package com.jpigeon.ridebattleparallelworlds.common.registry.block;


import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModBlockEntities;
import com.jpigeon.rideevolutionlib.compat.geckoLib.block.BaseRiderGeoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.*;

public class KuugaCoffinBlockEntity extends BaseRiderGeoBlockEntity {
    public KuugaCoffinBlockEntity(BlockPos pos, BlockState blockState) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "kuuga_coffin", ModBlockEntities.KUUGA_COFFIN_BE.get(), pos, blockState, true);
    }

    @Override
    protected void registerAnimationControllers(
            AnimatableManager.ControllerRegistrar registrar
    ) {
        AnimationController<BaseRiderGeoBlockEntity> controller =
                new AnimationController<>(
                        this,
                        "coffin_controller",
                        0,
                        state -> {
                            if (isOpen()) {
                                state.getController().setAnimation(
                                        RawAnimation.begin().then(
                                                "open",
                                                Animation.LoopType.HOLD_ON_LAST_FRAME
                                        )
                                );
                            } else {
                                state.getController().setAnimation(
                                        RawAnimation.begin().thenLoop("idle")
                                );
                            }

                            return PlayState.CONTINUE;
                        }
                );

        addController(registrar, "coffin", controller);
    }

    public boolean isOpen() {
        return getBlockState().getValue(KuugaCoffinBlock.OPEN);
    }
}
