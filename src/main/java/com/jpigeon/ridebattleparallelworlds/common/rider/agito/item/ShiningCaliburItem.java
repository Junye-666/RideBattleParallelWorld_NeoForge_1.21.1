package com.jpigeon.ridebattleparallelworlds.common.rider.agito.item;

import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.*;

public class ShiningCaliburItem extends BaseRiderGeoItem {
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

    private boolean isOpen() {
        return !this.getCurrentAnimState().equals("closed");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!isTransformed(player)) return InteractionResultHolder.pass(itemStack);

        if (canUse(player)) {
            if (Screen.hasShiftDown()) {
                if (level.isClientSide()) {
                    if (isOpen()) setClose();
                    else triggerOpen();
                }
                player.getCooldowns().addCooldown(this, 20);
                return InteractionResultHolder.pass(itemStack);

            } else if (usedHand.equals(InteractionHand.MAIN_HAND) && isOpen()) {
                player.getCooldowns().addCooldown(this, 610);
                triggerSkill(player, RiderSkills.BURNING_BOMBER, SkillEvent.SkillTriggerType.WEAPON);
            } else {
                return InteractionResultHolder.pass(itemStack);
            }
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public @Nullable Entity createEntity(Level level, @NotNull Entity location, @NotNull ItemStack stack) {
        if (level.isClientSide()) setClose();
        return super.createEntity(level, location, stack);
    }

    private boolean canUse(Player player) {
        return isSpecificRider(player, RiderIds.DECADE_ID) || isSpecificForm(player, AgitoConfig.BURNING_ID);
    }
}
