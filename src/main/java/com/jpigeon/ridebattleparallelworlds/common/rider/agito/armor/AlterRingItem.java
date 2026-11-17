package com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor;


import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseRiderArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

public class AlterRingItem extends BaseRiderArmorItem {
    public AlterRingItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "alter_ring", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "inBody", createLoopController("inBody"));
        addController(registrar, "appear", createHoldController("appear"));
        addController(registrar, "shrink", createHoldController("shrink"));
        addController(registrar, "ground", createLoopController("ground"));
        addController(registrar, "flame", createLoopController("flame"));
        addController(registrar, "storm", createLoopController("storm"));
        addController(registrar, "trinity", createLoopController("trinity"));
        addController(registrar, "burning", createLoopController("burning"));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.arcle.tutorial"));
        } else {
            tooltip.add(Component.translatable("tooltip.alterRing.description").withStyle(ChatFormatting.GOLD));
        }
    }

    @Nullable
    public String getAnimationStateByFormId(ResourceLocation formId) {
        if (formId == null) {
            return null;
        }

        if (formId.equals(AgitoConfig.GROUND_ID)) {
            return "ground";
        }

        if (formId.equals(AgitoConfig.FLAME_ID)) {
            return "flame";
        }

        if (formId.equals(AgitoConfig.STORM_ID)) {
            return "storm";
        }

        if (formId.equals(AgitoConfig.TRINITY_ID)) {
            return "trinity";
        }

        if (formId.equals(AgitoConfig.BURNING_ID)) {
            return "burning";
        }

        return null;
    }
}
