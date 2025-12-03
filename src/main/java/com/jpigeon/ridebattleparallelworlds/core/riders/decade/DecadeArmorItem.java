package com.jpigeon.ridebattleparallelworlds.core.riders.decade;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DecadeArmorItem extends BaseKamenRiderArmorItem {
    public DecadeArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("decade", material, type, properties);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        // 使用修正后的方法创建控制器
        addController(registrar, "idle", createLoopAnimationController("idle_controller", "idle"));
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
