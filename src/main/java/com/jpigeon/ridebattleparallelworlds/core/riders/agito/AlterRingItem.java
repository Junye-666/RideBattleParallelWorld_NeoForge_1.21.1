package com.jpigeon.ridebattleparallelworlds.core.riders.agito;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AlterRingItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IN_BODY, APPEAR, SHRINK, GROUND, FLAME, STORM, TRINITY, BURNING}
    private AnimState currentState;
    private boolean playingAnimation = false;

    public AlterRingItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("agito", "alter_ring", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "inBody", createInBodyController());
        addController(registrar, "appear", createAppearController());
        addController(registrar, "shrink", createShrinkController());
        addController(registrar, "ground", createGroundController());
        addController(registrar, "flame", createFireController());
        addController(registrar, "storm", createWindController());

        addController(registrar, "burning", createBurningController());
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

    private AnimationController<BaseKamenRiderArmorItem> createGroundController() {
        return new AnimationController<>(this, "ground_controller", 0, state -> {
            if (currentState == AnimState.GROUND) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("ground"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createFireController() {
        return new AnimationController<>(this, "flame_controller", 0, state -> {
            if (currentState == AnimState.FLAME) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("flame"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createWindController() {
        return new AnimationController<>(this, "storm_controller", 0, state -> {
            if (currentState == AnimState.STORM) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("storm"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderArmorItem> createBurningController() {
        return new AnimationController<>(this, "burning_controller", 0, state -> {
            if (currentState == AnimState.BURNING) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("burning_mighty"));
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
