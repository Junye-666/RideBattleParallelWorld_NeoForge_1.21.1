package com.jpigeon.ridebattleparallelworlds.core.riders.agito.item;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.BaseKamenRiderGeoItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.GenericItemModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.item.GenericItemRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ShiningCaliburItem extends BaseKamenRiderGeoItem {
    public enum AnimState {CLOSED, OPEN, OPENED}

    public ShiningCaliburItem(Properties properties) {
        super("agito", "shining_calibur", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "closed", createLoopController("closed"));
        addController(registrar, "open", createHoldController("open"));
        addController(registrar, "opened", createLoopController("opened"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }

    public void setClose() {
        setAnimState("closed");
    }

    @Override
    protected GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer() {
        return new GenericItemRenderer(
                new GenericItemModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    private boolean isOpen() {
        return !this.getCurrentAnimState().equals("closed");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RiderManager.isTransformed(player)) {
            if (RiderManager.isSpecificForm(player, AgitoConfig.BURNING_ID)) {
                if (Screen.hasShiftDown()) {
                    if (isOpen()) setClose();
                    else triggerOpen();
                    player.getCooldowns().addCooldown(this, 20);
                    return InteractionResultHolder.pass(itemStack);

                } else if (usedHand.equals(InteractionHand.MAIN_HAND) && isOpen()) {
                    player.getCooldowns().addCooldown(this, 610);
                    RiderManager.triggerSkill(player, RiderSkills.BURNING_BOMBER, SkillEvent.SkillTriggerType.WEAPON);
                } else {
                    return InteractionResultHolder.pass(itemStack);
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
