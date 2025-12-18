package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.BaseKamenRiderGeoItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericItemModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RisingPegasusBowgunItem extends BaseKamenRiderGeoItem {
    public RisingPegasusBowgunItem(Properties properties) {
        super("kuuga", "rising_pegasus_bowgun", properties.stacksTo(1).durability(0), true);
    }

    public enum AnimState {IDLE, PULL, RELEASE, SHOOT}
    private AnimState currentState;

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createIdleController());
        addController(registrar, "pull", createPullController());
        addController(registrar, "release", createReleaseController());
        addController(registrar, "shoot", createShootController());
    }

    private AnimationController<BaseKamenRiderGeoItem> createIdleController() {
        return new AnimationController<>(this, "idle_controller", 0, state -> {
            if (currentState == AnimState.IDLE) {
                state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderGeoItem> createPullController() {
        return new AnimationController<>(this, "pull_controller", 0, state -> {
            if (currentState == AnimState.PULL) {
                    state.getController().setAnimation(
                            RawAnimation.begin().thenPlay("pull")
                    );
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderGeoItem> createReleaseController() {
        return new AnimationController<>(this, "release_controller", 0, state -> {
            if (currentState == AnimState.RELEASE) {
                    state.getController().setAnimation(
                            RawAnimation.begin().thenPlay("release")
                    );
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    private AnimationController<BaseKamenRiderGeoItem> createShootController() {
        return new AnimationController<>(this, "shoot_controller", 0, state -> {
            if (currentState == AnimState.SHOOT) {
                state.getController().setAnimation(
                        RawAnimation.begin().thenPlay("shoot")
                );
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    public void triggerShoot() {
        setAnimState(AnimState.SHOOT);
    }

    public void triggerPull() {
        setAnimState(AnimState.PULL);
    }

    public void triggerRelease() {
        setAnimState(AnimState.RELEASE);
    }

    public void setCurrentState(AnimState state){
        setAnimState(state);
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

    @Override
    protected GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer() {
        return new GenericItemRenderer(
                new GenericItemModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RiderManager.isTransformed(player)) {
            if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_PEGASUS_ID)) {
                player.getCooldowns().addCooldown(this, 210);
                RiderManager.triggerSkill(player, RiderSkills.RISING_BLAST_PEGASUS);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
