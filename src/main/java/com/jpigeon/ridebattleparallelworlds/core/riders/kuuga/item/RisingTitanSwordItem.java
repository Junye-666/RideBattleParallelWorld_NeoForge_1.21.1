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

public class RisingTitanSwordItem extends BaseKamenRiderGeoItem {
    public RisingTitanSwordItem(Properties properties) {
        super("kuuga", "rising_titan_sword", properties.stacksTo(1).durability(0), true);
    }

    public enum AnimState {IDLE, STAB}
    private AnimState currentState;

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopAnimationController("idle_controller", "idle"));
        addController(registrar, "stab", createStabController());
    }

    private AnimationController<BaseKamenRiderGeoItem> createStabController() {
        return new AnimationController<>(this, "stab_controller", 0, state -> {
            if (currentState == AnimState.STAB) {
                state.getController().setAnimation(
                        RawAnimation.begin().thenPlay("stab")
                );
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
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

    public void triggerStab(){
        setAnimState(AnimState.STAB);
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
            if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_TITAN_ID)) {
                player.getCooldowns().addCooldown(this, 410);
                RiderManager.triggerSkill(player, RiderSkills.RISING_CALAMITY_TITAN);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
