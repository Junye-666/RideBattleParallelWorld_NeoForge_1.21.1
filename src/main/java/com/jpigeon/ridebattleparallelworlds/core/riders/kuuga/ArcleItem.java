package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
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

public class ArcleItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IN_BODY, APPEAR, SHRINK, MIGHTY, DRAGON, PEGASUS, TITAN, RISING_MIGHTY, RISING_DRAGON, RISING_PEGASUS, RISING_TITAN, AMAZING_MIGHTY, ULTIMATE}

    public ArcleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("kuuga", "arcle", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "inBody", createLoopController("inBody"));
        addController(registrar, "appear", createHoldController("appear"));
        addController(registrar, "shrink", createHoldController("shrink"));
        addController(registrar, "mighty", createLoopController("mighty"));
        addController(registrar, "dragon", createLoopController("dragon"));
        addController(registrar, "pegasus", createLoopController("pegasus"));
        addController(registrar, "titan", createLoopController("titan"));

        addController(registrar, "rising_mighty", createLoopController("rising_mighty"));
        addController(registrar, "rising_dragon", createLoopController("rising_dragon"));
        addController(registrar, "rising_pegasus", createLoopController("rising_pegasus"));
        addController(registrar, "rising_titan", createLoopController("rising_titan"));

        addController(registrar, "amazing_mighty", createLoopController("amazing_mighty"));
        addController(registrar, "ultimate", createLoopController("ultimate"));
    }

    // 变身方法
    public void triggerAppear() {
        setAnimState("appear");
    }

    public void shrinkInBody() {
        setAnimState("shrink");
    }

    public void setCurrentState(AnimState state) {
        setAnimState(state.name().toLowerCase());
    }

    public void setStateByFormId(ResourceLocation formId) {
        if (formId.equals(KuugaConfig.MIGHTY_ID)) {
            setAnimState("mighty");
        } else if (formId.equals(KuugaConfig.DRAGON_ID)) {
            setAnimState("dragon");
        } else if (formId.equals(KuugaConfig.PEGASUS_ID)) {
            setAnimState("pegasus");
        } else if (formId.equals(KuugaConfig.TITAN_ID)) {
            setAnimState("titan");
        } else if (formId.equals(KuugaConfig.RISING_MIGHTY_ID)) {
            setAnimState("rising_mighty");
        } else if (formId.equals(KuugaConfig.RISING_DRAGON_ID)) {
            setAnimState("rising_dragon");
        } else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
            setAnimState("rising_pegasus");
        } else if (formId.equals(KuugaConfig.RISING_TITAN_ID)) {
            setAnimState("rising_titan");
        } else if (formId.equals(KuugaConfig.AMAZING_MIGHTY_ID)) {
            setAnimState("amazing_mighty");
        } else if (formId.equals(KuugaConfig.ULTIMATE_ID)) {
            setAnimState("ultimate");
        }
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
            tooltip.add(Component.translatable("tooltip.arcle.description"));
        }
    }
}
