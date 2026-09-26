package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

public class RisingTitanSwordItem extends BaseRiderGeoItem {
    public RisingTitanSwordItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "rising_titan_sword", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "stab", createOnceController("stab"));
    }

    public void triggerStab(){
        setAnimState("stab");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RideBattleAPI.isTransformed(player)) {
            if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_TITAN_ID)) {
                player.getCooldowns().addCooldown(this, 410);
                triggerStab();
                RideBattleAPI.triggerSkill(player, RiderSkills.RISING_CALAMITY_TITAN, SkillEvent.SkillTriggerType.WEAPON);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
