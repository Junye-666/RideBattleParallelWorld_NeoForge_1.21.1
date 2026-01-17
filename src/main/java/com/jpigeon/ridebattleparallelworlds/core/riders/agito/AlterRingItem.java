package com.jpigeon.ridebattleparallelworlds.core.riders.agito;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

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
        if (formId.equals(AgitoConfig.GROUND_ID)) {
            setAnimState("ground");
        } else if (formId.equals(AgitoConfig.FLAME_ID)) {
            setAnimState("flame");
        } else if (formId.equals(AgitoConfig.STORM_ID)) {
            setAnimState("storm");
        }
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
