package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.rideevolutionlib.compat.util.UniversalRiderArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

public class ParallelRiderArmor extends UniversalRiderArmorItem {
    public ParallelRiderArmor(String modId, String riderName, String formName, Holder<ArmorMaterial> material, Type type, Properties properties, boolean animated) {
        super(modId, riderName, formName, material, type, properties, animated);
    }

    @Override
    public String getDescriptionId() {
        return "armor." + modId + "." + riderName + "." + formName;
    }
}
