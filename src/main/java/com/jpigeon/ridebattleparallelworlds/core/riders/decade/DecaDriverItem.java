package com.jpigeon.ridebattleparallelworlds.core.riders.decade;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class DecaDriverItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IDLE, OPEN, CLOSE}

    public DecaDriverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("decade", "deca_driver", material, type, properties, true);
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

    public void setCurrentState(AnimState state) {
        setAnimState(state.name().toLowerCase());
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
        tooltip.add(Component.translatable("tooltip.decaDriver.description"));
    }
}
