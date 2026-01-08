package com.jpigeon.ridebattleparallelworlds.core.riders.agito.armor;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AgitoGroundItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IDLE, OPEN, POWERED}
    private AnimState currentState = AnimState.IDLE;
    private boolean playingAnimation = false;

    public AgitoGroundItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("agito", "ground", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createIdleController());
        addController(registrar, "powered", createPoweredController());
        addController(registrar, "open", createOpenController());
    }

    private AnimationController<BaseKamenRiderArmorItem> createIdleController() {
        return new AnimationController<>(this, "idle_controller", 0, state -> {
            if (currentState == AnimState.IDLE) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createPoweredController() {
        return new AnimationController<>(this, "powered_controller", 0, state -> {
            if (currentState == AnimState.POWERED) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("powered"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createOpenController() {
        return new AnimationController<>(this, "open_controller", 0, state -> {
            if (currentState == AnimState.OPEN) {
                if (!playingAnimation) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("appear", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    playingAnimation = true;
                }

                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    playingAnimation = false;
                }
                return PlayState.CONTINUE;
            }
            playingAnimation = false;
            return PlayState.STOP;
        });
    }

    private void setAnimState(AnimState state) {
        if (currentState != state) {
            currentState = state;
            controllers.values().forEach(controller -> {
                if (controller != null) {
                    controller.forceAnimationReset();
                }
            });
        }
    }

    public void triggerOpen() {
        setAnimState(AnimState.OPEN);
    }

    public void setClosed() {
        setAnimState(AnimState.IDLE);
    }

    public void setCurrentState(AnimState state){
        setAnimState(state);
    }

    public AnimState getCurrentState() {
        return currentState;
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
