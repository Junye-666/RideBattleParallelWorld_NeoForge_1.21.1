package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcleItem extends BaseKamenRiderArmorItem {
    public enum AnimState {MIGHTY, DRAGON, PEGASUS, TITAN, IN_BODY, APPEAR, SHRINK}
    private AnimState currentState;
    private boolean isAppearAnimationPlaying = false;
    private boolean isShrinkAnimationPlaying = false;

    public ArcleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("kuuga", "arcle", material, type, properties);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "mighty", createMightyController());
        addController(registrar, "dragon", createDragonController());
        addController(registrar, "pegasus", createPegasusController());
        addController(registrar, "titan", createTitanController());
        addController(registrar, "inbody", createInBodyController());
        addController(registrar, "appear", createAppearController());
        addController(registrar, "shrink", createShrinkController());
    }

    private AnimationController<BaseKamenRiderArmorItem> createMightyController() {
        return new AnimationController<>(this, "mighty_controller", 0, state -> {
            if (currentState == AnimState.MIGHTY) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("mighty"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createDragonController() {
        return new AnimationController<>(this, "dragon_controller", 0, state -> {
            if (currentState == AnimState.DRAGON) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("dragon"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createPegasusController() {
        return new AnimationController<>(this, "pegasus_controller", 0, state -> {
            if (currentState == AnimState.PEGASUS) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("pegasus"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createTitanController() {
        return new AnimationController<>(this, "titan_controller", 0, state -> {
            if (currentState == AnimState.TITAN) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("titan"));
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
                    isAppearAnimationPlaying = false;
                }
                return PlayState.CONTINUE;
            }
            isAppearAnimationPlaying = false;
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createShrinkController() {
        return new AnimationController<>(this, "shrink_controller", 0, state -> {
            if (currentState == AnimState.SHRINK) {
                if (!isShrinkAnimationPlaying) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("shrink", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    isShrinkAnimationPlaying = true;
                }

                // 检查动画是否播放完成
                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    setAnimState(AnimState.IN_BODY);
                    isShrinkAnimationPlaying = false;
                }
                return PlayState.CONTINUE;
            }
            isShrinkAnimationPlaying = false;
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

    public void shrinkInBody() {
        setAnimState(AnimState.SHRINK);
    }

    public void setCurrentState(AnimState state){
        setAnimState(state);
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
