package com.jpigeon.ridebattleparallelworlds.common.rider.decade;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseRiderArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

public class DecaDriverItem extends BaseRiderArmorItem {
    public DecaDriverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "decade", "deca_driver", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
        addController(registrar, "close", createHoldController("close"));
    }

    // 变身方法
    public void triggerOpen() {
        setAnimState("open");
    }

    public void triggerClose() {
        setAnimState("close");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        tooltip.add(Component.translatable("tooltip.decaDriver.description").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
