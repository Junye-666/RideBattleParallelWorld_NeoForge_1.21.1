package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;

import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseKamenRiderGeoItem;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.GenericItemModel;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.GenericItemRenderer;
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
    public ShiningCaliburItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "shining_calibur", properties.stacksTo(1).durability(0), true);
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
        if (!level.isClientSide() && RideBattleAPI.isTransformed(player)) {
            if (RideBattleAPI.isSpecificForm(player, AgitoConfig.BURNING_ID)) {
                if (Screen.hasShiftDown()) {
                    if (isOpen()) setClose();
                    else triggerOpen();
                    player.getCooldowns().addCooldown(this, 20);
                    return InteractionResultHolder.pass(itemStack);

                } else if (usedHand.equals(InteractionHand.MAIN_HAND) && isOpen()) {
                    player.getCooldowns().addCooldown(this, 610);
                    RideBattleAPI.triggerSkill(player, RiderSkills.BURNING_BOMBER, SkillEvent.SkillTriggerType.WEAPON);
                } else {
                    return InteractionResultHolder.pass(itemStack);
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
