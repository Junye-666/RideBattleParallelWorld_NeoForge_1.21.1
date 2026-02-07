package com.jpigeon.ridebattleparallelworlds.core.riders.agito;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class AlterRingItem extends BaseKamenRiderArmorItem {
    public enum AnimState {APPEAR, SHRINK, GROUND, FLAME, STORM, TRINITY, BURNING}

    public AlterRingItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("agito", "alter_ring", material, type, properties, true);
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

    // 变身方法
    public void triggerAppear() {
        setAnimState("appear");
    }

    public void shrinkInBody() {
        setAnimState("shrink");
    }

    public void setStateByFormId(ResourceLocation formId) {
        if (formId == null) return;
        if (formId.equals(AgitoConfig.GROUND_ID)) setAnimState("ground");
        else if (formId.equals(AgitoConfig.FLAME_ID)) setAnimState("flame");
        else if (formId.equals(AgitoConfig.STORM_ID)) setAnimState("storm");
        else if (formId.equals(AgitoConfig.TRINITY_ID)) setAnimState("trinity");
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.arcle.tutorial"));
        } else {
            tooltip.add(Component.translatable("tooltip.alterRing.description").withStyle(ChatFormatting.GOLD));
        }

    }
}
