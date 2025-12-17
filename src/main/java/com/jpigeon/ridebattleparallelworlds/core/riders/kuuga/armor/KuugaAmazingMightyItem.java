package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.armor;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class KuugaAmazingMightyItem extends BaseKamenRiderArmorItem {
    public KuugaAmazingMightyItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("kuuga", "amazing_mighty", material, type, properties, false);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopAnimationController("idle_controller", "idle"));
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
