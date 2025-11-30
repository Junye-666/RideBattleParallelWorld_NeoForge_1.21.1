package com.jpigeon.ridebattleparallelworlds.riders.kuuga;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattleparallelworlds.geckoLib.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.geckoLib.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.geckoLib.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcleItem extends BaseKamenRiderArmorItem {
    public enum AnimState { IDLE, IN_BODY, APPEAR}
    private AnimState currentState;
    private boolean isAppearAnimationPlaying = false;

    public ArcleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("arcle", material, type, properties);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createIdleController());
        addController(registrar, "inbody", createInBodyController());
        addController(registrar, "appear", createAppearController());
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

    private AnimationController<BaseKamenRiderArmorItem> createInBodyController() {
        return new AnimationController<>(this, "inbody_controller", 0, state -> {
            if (currentState == AnimState.IN_BODY) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("inBody"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createAppearController() {
        return new AnimationController<>(this, "appear_controller", 0, state -> {
            if (currentState == AnimState.APPEAR) {
                if (!isAppearAnimationPlaying) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("appear", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    isAppearAnimationPlaying = true;
                }

                // 检查动画是否播放完成
                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    setAnimState(AnimState.IDLE);
                    isAppearAnimationPlaying = false;
                }
                return PlayState.CONTINUE;
            }
            isAppearAnimationPlaying = false;
            return PlayState.STOP;
        });
    }

    public void setAnimState(AnimState state) {
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
    public void triggerAppear() {
        setAnimState(AnimState.APPEAR);
    }

    public void resetToInBody() {
        setAnimState(AnimState.IN_BODY);
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
