package com.jpigeon.ridebattleparallelworlds.core.riders.decade;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DecaDriverItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IDLE, OPEN, CLOSE}
    private AnimState currentState;
    private boolean playingAnimation = false;

    public DecaDriverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("decade", "deca_driver", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createIdleController());
        addController(registrar, "open", createOpenController());
        addController(registrar, "close", createShrinkController());
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

    private AnimationController<BaseKamenRiderArmorItem> createOpenController() {
        return new AnimationController<>(this, "open_controller", 0, state -> {
            if (currentState == AnimState.OPEN) {
                if (!playingAnimation) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("open", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    playingAnimation = true;
                }

                // 检查动画是否播放完成
                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    playingAnimation = false;
                }
                return PlayState.CONTINUE;
            }
            playingAnimation = false;
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createShrinkController() {
        return new AnimationController<>(this, "close_controller", 0, state -> {
            if (currentState == AnimState.CLOSE) {
                if (!playingAnimation) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("close", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    playingAnimation = true;
                }

                // 检查动画是否播放完成
                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    setAnimState(AnimState.IDLE);
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
            // 重置所有控制器以确保动画重新开始
            controllers.values().forEach(controller -> {
                if (controller != null) {
                    controller.forceAnimationReset();
                }
            });
        }
    }

    // 变身方法
    public void triggerOpen() {
        setAnimState(AnimState.OPEN);
    }

    public void triggerClose() {
        setAnimState(AnimState.CLOSE);
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
