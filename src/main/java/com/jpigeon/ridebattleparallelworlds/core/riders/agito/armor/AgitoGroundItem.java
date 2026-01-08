package com.jpigeon.ridebattleparallelworlds.core.riders.agito.armor;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AgitoGroundItem extends BaseKamenRiderArmorItem {
    public enum AnimState {IDLE, OPEN, POWERED}

    public AgitoGroundItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super("agito", "ground", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }

    public void setClosed() {
        setAnimState("idle");
    }

    public void setCurrentState(AnimState state){
        setAnimState(state.name().toLowerCase());
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
