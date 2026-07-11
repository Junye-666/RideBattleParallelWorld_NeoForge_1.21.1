package com.jpigeon.ridebattleparallelworlds.core.common.registry.extra.shocker;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseKamenRiderArmorItem;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.GenericArmorModel;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.GenericArmorRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ShockerCombatManItem extends BaseKamenRiderArmorItem {
    public ShockerCombatManItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "shocker", "combatman", material, type, properties, false);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
    }

    @Override
    protected GeoArmorRenderer<?> createRenderer() {
        return new GenericArmorRenderer(
                new GenericArmorModel(getModelPath(), getTexturePath(), getAnimationPath())
        );
    }
}
