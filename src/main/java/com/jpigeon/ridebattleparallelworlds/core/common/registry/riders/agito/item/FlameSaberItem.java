package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;

public class FlameSaberItem extends BaseRiderGeoItem {
    public FlameSaberItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "flame_saber", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }

    public void setClose() {
        setAnimState("idle");
    }

    @Override
    public @Nullable Entity createEntity(Level level, @NotNull Entity location, @NotNull ItemStack stack) {
        if (level.isClientSide()) setClose();
        return super.createEntity(level, location, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!RideBattleAPI.isTransformed(player)) return InteractionResultHolder.pass(itemStack);
        if (RideBattleAPI.isSpecificForm(player, AgitoConfig.FLAME_ID) || RideBattleAPI.isSpecificForm(player, AgitoConfig.TRINITY_ID)) {

            if (usedHand.equals(InteractionHand.MAIN_HAND)) {
                if (level.isClientSide()) triggerOpen();
                if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof StormHalberdItem stormHalberd) {
                    if (level.isClientSide()) stormHalberd.triggerOpen();

                    player.getCooldowns().addCooldown(this, 410);
                    player.getCooldowns().addCooldown(stormHalberd, 410);
                    RideBattleAPI.triggerSkill(player, RiderSkills.FIRESTORM_ATTACK, SkillEvent.SkillTriggerType.WEAPON);
                    return InteractionResultHolder.success(itemStack);
                } else {
                    player.getCooldowns().addCooldown(this, 310);
                    RideBattleAPI.triggerSkill(player, RiderSkills.SABER_SLASH, SkillEvent.SkillTriggerType.WEAPON);
                }
            } else {
                return InteractionResultHolder.pass(itemStack);
            }
        }

        return InteractionResultHolder.success(itemStack);
    }
}
