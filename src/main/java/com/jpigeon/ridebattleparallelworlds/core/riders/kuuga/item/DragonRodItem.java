package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DragonRodItem extends BaseKamenRiderGeoItem {
    public DragonRodItem(Properties properties) {
        super("kuuga", "dragon_rod", properties.stacksTo(1).durability(0), true);
    }

    public enum AnimState {IDLE, SPIN_MAIN_HAND, SPIN_OFF_HAND}
    private AnimState currentState;

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createIdleController());
        addController(registrar, "spin_main", createSpinMainController());

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

    private AnimationController<BaseKamenRiderGeoItem> createSpinMainController() {
        return new AnimationController<>(this, "spin_main_controller", 0, state -> {
            if (currentState == AnimState.SPIN_MAIN_HAND) {
                state.getController().setAnimation(RawAnimation.begin().then("spin_main", Animation.LoopType.PLAY_ONCE));
            }
            return PlayState.STOP;
        });
    }

    public void triggerMainSpin() {
        setAnimState(AnimState.SPIN_MAIN_HAND);
    }

    public void triggerOffSpin() {
        setAnimState(AnimState.SPIN_OFF_HAND);
    }

    public void setCurrentState(AnimState state){
        setAnimState(state);
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

    @Override
    protected GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer() {
        return new GenericItemRenderer(
                new GenericItemModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide()) {
            player.getCooldowns().addCooldown(this, 300);
            RiderManager.triggerSkill(player, RiderSkills.SPLASH_DRAGON);
        }
        return InteractionResultHolder.success(itemStack);
    }
}
