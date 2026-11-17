package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.weapon.RiderWeaponItem;
import com.jpigeon.rideevolutionlib.compat.geckoLib.GeoAnimationApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.Map;

public class RisingTitanSwordItem extends RiderWeaponItem {
    public RisingTitanSwordItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "rising_titan_sword", properties.stacksTo(1).durability(0));
    }

    @Override
    protected List<ResourceLocation> requiredForms() {
        return List.of(KuugaConfig.RISING_TITAN_ID, RiderIds.DECADE_ID);
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
    protected void onUse(ItemStack stack, Player player, InteractionHand hand) {
        GeoAnimationApi.trigger(
                this,
                stack,
                player,
                "actions",
                "stab"
        );
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "actions",
                createTriggerController("actions",
                        Map.of(
                                "stab",
                                RawAnimation.begin().then(
                                        "stab",
                                        Animation.LoopType.PLAY_ONCE
                                )
                        )
                )
        );
    }
}
