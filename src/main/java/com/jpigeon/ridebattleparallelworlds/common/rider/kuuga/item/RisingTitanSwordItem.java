package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.weapon.RiderWeaponItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimatableManager;

public class RisingTitanSwordItem extends RiderWeaponItem {
    public RisingTitanSwordItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "rising_titan_sword", properties.stacksTo(1).durability(0));
    }

    @Override
    protected ResourceLocation requiredForm() {
        return KuugaConfig.RISING_TITAN_ID;
    }

    @Override
    protected ResourceLocation skill() {
        return RiderSkills.RISING_CALAMITY_TITAN;
    }

    @Override
    protected int cooldownTicks() {
        return 410;
    }

    @Override
    protected void onUse(Player player, InteractionHand hand) {
        setAnimState("stab");
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "stab", createOnceController("stab"));
    }
}
