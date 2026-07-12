package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseKamenRiderGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

public class RisingDragonRodItem extends BaseKamenRiderGeoItem {
    public RisingDragonRodItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "rising_dragon_rod", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "spin_main", createOnceController("spin_main"));
        addController(registrar, "spin_off", createOnceController("spin_off"));

    }

    public void triggerMainSpin() {
        setAnimState("spin_main");
    }

    public void triggerOffSpin() {
        setAnimState("spin_off");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RideBattleAPI.isTransformed(player)) {
            if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_DRAGON_ID)) {
                player.getCooldowns().addCooldown(this, 410);
                if (usedHand.equals(InteractionHand.MAIN_HAND)) {
                    triggerMainSpin();
                } else triggerOffSpin();
                RideBattleAPI.triggerSkill(player, RiderSkills.RISING_SPLASH_DRAGON, SkillEvent.SkillTriggerType.WEAPON);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
