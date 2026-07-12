package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseKamenRiderGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

public class StormHalberdItem extends BaseKamenRiderGeoItem {
    public StormHalberdItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "storm_halberd", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen(){
        setAnimState("open");
    }
    public void setClose(){
        setAnimState("idle");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RideBattleAPI.isTransformed(player)) {
            if (RideBattleAPI.isSpecificForm(player, AgitoConfig.STORM_ID) || RideBattleAPI.isSpecificForm(player, AgitoConfig.TRINITY_ID)) {
                if (usedHand.equals(InteractionHand.MAIN_HAND)) {
                    player.getCooldowns().addCooldown(this, 310);
                    triggerOpen();
                    RideBattleAPI.triggerSkill(player, RiderSkills.HALBERD_SPIN, SkillEvent.SkillTriggerType.WEAPON);
                } else {
                    return InteractionResultHolder.pass(itemStack);
                }

            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
