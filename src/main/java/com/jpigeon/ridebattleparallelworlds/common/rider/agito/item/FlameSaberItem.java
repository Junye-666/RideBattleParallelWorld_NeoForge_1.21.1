package com.jpigeon.ridebattleparallelworlds.common.rider.agito.item;

import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.GeoAnimationApi;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.*;

public class FlameSaberItem extends BaseRiderGeoItem {
    public FlameSaberItem(Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "flame_saber", properties.stacksTo(1).durability(0), true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (!isTransformed(player)) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (!canUse(player)) {
            return InteractionResultHolder.success(itemStack);
        }

        if (usedHand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(itemStack);
        }

        // Flame Saber 展开
        if (!level.isClientSide()) {
            GeoAnimationApi.setState(this, itemStack, player, "open");
        }

        ItemStack offhandStack = player.getOffhandItem();

        // 同时持有 Storm Halberd：触发 Firestorm Attack
        if (offhandStack.getItem() instanceof StormHalberdItem stormHalberd) {
            if (!level.isClientSide()) {
                GeoAnimationApi.setState(
                        stormHalberd,
                        offhandStack,
                        player,
                        "open"
                );
            }

            player.getCooldowns().addCooldown(this, 410);
            player.getCooldowns().addCooldown(stormHalberd, 410);

            triggerSkill(
                    player,
                    RiderSkills.FIRESTORM_ATTACK,
                    SkillEvent.SkillTriggerType.WEAPON
            );

            return InteractionResultHolder.success(itemStack);
        }

        player.getCooldowns().addCooldown(this, 310);

        triggerSkill(
                player,
                RiderSkills.SABER_SLASH,
                SkillEvent.SkillTriggerType.WEAPON
        );

        return InteractionResultHolder.success(itemStack);
    }

    private boolean canUse(Player player) {
        return isSpecificRider(player, RiderIds.DECADE_ID)
                || isSpecificForm(player, AgitoConfig.FLAME_ID)
                || isSpecificForm(player, AgitoConfig.TRINITY_ID);
    }
}
