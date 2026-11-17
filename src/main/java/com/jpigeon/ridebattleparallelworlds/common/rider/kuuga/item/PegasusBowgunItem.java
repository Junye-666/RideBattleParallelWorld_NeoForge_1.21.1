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

public class PegasusBowgunItem extends RiderWeaponItem {
    public PegasusBowgunItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "pegasus_bowgun", properties.stacksTo(1).durability(0));
    }

    @Override
    protected List<ResourceLocation> requiredForms() {
        return List.of(KuugaConfig.PEGASUS_ID, RiderIds.DECADE_ID);
    }

    @Override
    protected ResourceLocation skill() {
        return RiderSkills.BLAST_PEGASUS;
    }

    @Override
    protected int cooldownTicks() {
        return 110;
    }

    @Override
    protected void onUse(ItemStack stack, Player player, InteractionHand hand) {
        GeoAnimationApi.trigger(
                this,
                stack,
                player,
                "actions",
                "shoot"
        );
    }
    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "pull", createLoopController("pull"));
        addController(registrar, "release", createLoopController("release"));

        addController(registrar, "actions",
                createTriggerController("actions",
                        Map.of(
                                "shoot",
                                RawAnimation.begin().then(
                                        "shoot",
                                        Animation.LoopType.PLAY_ONCE
                                )
                        )
                )
        );
    }
}
