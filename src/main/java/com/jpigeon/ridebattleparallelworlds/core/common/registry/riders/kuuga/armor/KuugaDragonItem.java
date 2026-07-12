package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.armor;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseKamenRiderArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animation.AnimatableManager;

public class KuugaDragonItem extends BaseKamenRiderArmorItem {
    public KuugaDragonItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(RideBattleParallelWorlds.MODID, "kuuga", "dragon", material, type, properties, false);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
    }
}
