package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.weapon.RiderWeaponItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimatableManager;

public class DragonRodItem extends RiderWeaponItem {
    public DragonRodItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "dragon_rod", properties.stacksTo(1).durability(0));
    }

    @Override
    protected ResourceLocation requiredForm() {
        return KuugaConfig.DRAGON_ID;
    }

    @Override
    protected ResourceLocation skill() {
        return RiderSkills.SPLASH_DRAGON;
    }

    @Override
    protected int cooldownTicks() {
        return 310;
    }

    @Override
    protected void onUse(Player player, InteractionHand hand) {
        setAnimState(hand.equals(InteractionHand.MAIN_HAND) ? "spin_main" : "spin_off");
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "spin_main", createOnceController("spin_main"));
        addController(registrar, "spin_off", createOnceController("spin_off"));
    }
}
