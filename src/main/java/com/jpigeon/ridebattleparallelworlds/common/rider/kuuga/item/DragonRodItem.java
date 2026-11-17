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

public class DragonRodItem extends RiderWeaponItem {
    public DragonRodItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "dragon_rod", properties.stacksTo(1).durability(0));
    }

    @Override
    protected List<ResourceLocation> requiredForms() {
        return List.of(KuugaConfig.DRAGON_ID, RiderIds.DECADE_ID);
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
    protected void onUse(ItemStack stack, Player player, InteractionHand hand) {
        GeoAnimationApi.trigger(
                this,
                stack,
                player,
                "actions",
                hand == InteractionHand.MAIN_HAND ? "spin_main" : "spin_off"
        );
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "actions",
                createTriggerController("actions",
                        Map.of(
                                "spin_main",
                                RawAnimation.begin().then(
                                        "spin_main",
                                        Animation.LoopType.PLAY_ONCE
                                ),
                                "spin_off",
                                RawAnimation.begin().then(
                                        "spin_off",
                                        Animation.LoopType.PLAY_ONCE
                                )
                        )
                )
        );
    }
}
