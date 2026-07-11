package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseKamenRiderGeoItem;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.GenericItemModel;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.GenericItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RisingPegasusBowgunItem extends BaseKamenRiderGeoItem {
    public RisingPegasusBowgunItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "rising_pegasus_bowgun", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "pull", createLoopController("pull"));
        addController(registrar, "release", createLoopController("release"));
        addController(registrar, "shoot", createOnceController("shoot"));
    }

    public void triggerShoot() {
        setAnimState("shoot");
    }

    public void triggerPull() {
        setAnimState("pull");
    }

    public void triggerRelease() {
        setAnimState("release");
    }

    @Override
    protected GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer() {
        return new GenericItemRenderer(
                new GenericItemModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide() && RideBattleAPI.isTransformed(player)) {
            if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_PEGASUS_ID)) {
                player.getCooldowns().addCooldown(this, 210);
                triggerShoot();
                RideBattleAPI.triggerSkill(player, RiderSkills.RISING_BLAST_PEGASUS, SkillEvent.SkillTriggerType.WEAPON);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
