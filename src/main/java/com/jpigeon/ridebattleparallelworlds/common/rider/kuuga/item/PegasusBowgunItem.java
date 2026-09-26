package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.weapon.RiderWeaponItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimatableManager;

public class PegasusBowgunItem extends RiderWeaponItem {
    public PegasusBowgunItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "pegasus_bowgun", properties.stacksTo(1).durability(0));
    }

    @Override
    protected ResourceLocation requiredForm() {
        return KuugaConfig.PEGASUS_ID;
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
    protected void onUse(Player player, InteractionHand hand) {
        setAnimState("shoot");
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "pull", createLoopController("pull"));
        addController(registrar, "release", createLoopController("release"));
        addController(registrar, "shoot", createOnceController("shoot"));
    }
}
