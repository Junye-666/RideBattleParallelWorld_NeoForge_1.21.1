package com.jpigeon.ridebattleparallelworlds.common.rider.weapon;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Kuuga 系列武器通用基类：
 * 统一处理 客户端跳过 / 变身检查 / 形态校验 / 冷却 / 技能触发。
 * 子类只需声明 requiredForm / skill / cooldownTicks 及动画。
 */
public abstract class RiderWeaponItem extends BaseRiderGeoItem {

    protected RiderWeaponItem(String modId, String rider, String item, Properties props) {
        super(modId, rider, item, props.stacksTo(1).durability(0), true);
    }

    /** 使用该武器必须变身的形态ID。 */
    protected abstract ResourceLocation requiredForm();

    /** 使用后触发的技能ID。 */
    protected abstract ResourceLocation skill();

    /** 冷却 tick 数，默认 300。 */
    protected int cooldownTicks() {
        return 300;
    }

    /** 技能触发类型，默认 WEAPON。 */
    protected SkillEvent.SkillTriggerType triggerType() {
        return SkillEvent.SkillTriggerType.WEAPON;
    }

    /** 客户端与服务端都会调用；只有非客户端、非变身、非目标形态才会跳过。 */
    protected void onUse(Player player, InteractionHand hand) {
    }

    @Override
    public final @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResultHolder.success(stack);
        if (!RideBattleAPI.isTransformed(player)) return InteractionResultHolder.success(stack);
        if (!RideBattleAPI.isSpecificForm(player, requiredForm())) {
            return InteractionResultHolder.success(stack);
        }

        player.getCooldowns().addCooldown(this, cooldownTicks());
        onUse(player, hand);
        RideBattleAPI.triggerSkill(player, skill(), triggerType());
        return InteractionResultHolder.success(stack);
    }
}