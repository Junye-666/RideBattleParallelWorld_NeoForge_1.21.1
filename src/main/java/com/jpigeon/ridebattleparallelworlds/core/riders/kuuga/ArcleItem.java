package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArcleItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IN_BODY, APPEAR, SHRINK, MIGHTY, DRAGON, PEGASUS, TITAN, RISING_MIGHTY, RISING_DRAGON, RISING_PEGASUS, RISING_TITAN, AMAZING_MIGHTY, ULTIMATE}
    private AnimState currentState;
    private boolean playingAnimation = false;

    public ArcleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("kuuga", "arcle", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "inBody", createInBodyController());
        addController(registrar, "appear", createAppearController());
        addController(registrar, "shrink", createShrinkController());
        addController(registrar, "mighty", createMightyController());
        addController(registrar, "dragon", createDragonController());
        addController(registrar, "pegasus", createPegasusController());
        addController(registrar, "titan", createTitanController());

        addController(registrar, "rising_mighty", createRisingMightyController());
        addController(registrar, "rising_dragon", createRisingDragonController());
        addController(registrar, "rising_pegasus", createRisingPegasusController());
        addController(registrar, "rising_titan", createRisingTitanController());

        addController(registrar, "amazing_mighty", createAmazingMightyController());
        addController(registrar, "ultimate", createUltimateController());
    }

    private AnimationController<BaseKamenRiderArmorItem> createInBodyController() {
        return new AnimationController<>(this, "inBody_controller", 0, state -> {
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
                if (!playingAnimation) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("appear", Animation.LoopType.HOLD_ON_LAST_FRAME)
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
        return new AnimationController<>(this, "shrink_controller", 0, state -> {
            if (currentState == AnimState.SHRINK) {
                if (!playingAnimation) {
                    state.getController().setAnimation(
                            RawAnimation.begin().then("shrink", Animation.LoopType.HOLD_ON_LAST_FRAME)
                    );
                    playingAnimation = true;
                }

                // 检查动画是否播放完成
                if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
                    setAnimState(AnimState.IN_BODY);
                    playingAnimation = false;
                }
                return PlayState.CONTINUE;
            }
            playingAnimation = false;
            return PlayState.STOP;
        });
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

    private AnimationController<BaseKamenRiderArmorItem> createRisingMightyController() {
        return new AnimationController<>(this, "rising_mighty_controller", 0, state -> {
            if (currentState == AnimState.RISING_MIGHTY) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("rising_mighty"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createRisingDragonController() {
        return new AnimationController<>(this, "rising_dragon_controller", 0, state -> {
            if (currentState == AnimState.RISING_DRAGON) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("rising_dragon"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createRisingPegasusController() {
        return new AnimationController<>(this, "rising_pegasus_controller", 0, state -> {
            if (currentState == AnimState.RISING_PEGASUS) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("rising_pegasus"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createRisingTitanController() {
        return new AnimationController<>(this, "rising_titan_controller", 0, state -> {
            if (currentState == AnimState.RISING_TITAN) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("rising_titan"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createAmazingMightyController() {
        return new AnimationController<>(this, "amazing_mighty_controller", 0, state -> {
            if (currentState == AnimState.AMAZING_MIGHTY) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("amazing_mighty"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createUltimateController() {
        return new AnimationController<>(this, "ultimate_controller", 0, state -> {
            if (currentState == AnimState.ULTIMATE) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("ultimate"));
                return PlayState.CONTINUE;
            }
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
    public void triggerAppear() {
        setAnimState(AnimState.APPEAR);
    }

    public void shrinkInBody() {
        setAnimState(AnimState.SHRINK);
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
