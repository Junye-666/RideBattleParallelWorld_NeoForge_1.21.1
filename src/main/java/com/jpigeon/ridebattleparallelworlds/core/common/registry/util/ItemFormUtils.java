package com.jpigeon.ridebattleparallelworlds.core.common.registry.util;

import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemFormUtils {
    public static final Map<Item, ResourceLocation> RIDER_ITEM_FORM_MAP = new HashMap<>();

    static {
        RIDER_ITEM_FORM_MAP.put(ModItems.MIGHTY_ELEMENT.get(), KuugaConfig.MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.DRAGON_ELEMENT.get(), KuugaConfig.DRAGON_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.PEGASUS_ELEMENT.get(), KuugaConfig.PEGASUS_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.TITAN_ELEMENT.get(), KuugaConfig.TITAN_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.RISING_MIGHTY_ELEMENT.get(), KuugaConfig.RISING_MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.RISING_DRAGON_ELEMENT.get(), KuugaConfig.RISING_DRAGON_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.RISING_PEGASUS_ELEMENT.get(), KuugaConfig.RISING_PEGASUS_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.RISING_TITAN_ELEMENT.get(), KuugaConfig.RISING_TITAN_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.AMAZING_MIGHTY_ELEMENT.get(), KuugaConfig.AMAZING_MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.ULTIMATE_ELEMENT.get(), KuugaConfig.ULTIMATE_ID);

        RIDER_ITEM_FORM_MAP.put(ModItems.GROUND_ELEMENT.get(), AgitoConfig.GROUND_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.FLAME_ELEMENT.get(), AgitoConfig.FLAME_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.STORM_ELEMENT.get(), AgitoConfig.STORM_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.TRINITY_ELEMENT.get(), AgitoConfig.TRINITY_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.BURNING_ELEMENT.get(), AgitoConfig.BURNING_ID);
        RIDER_ITEM_FORM_MAP.put(ModItems.SHINING_ELEMENT.get(), AgitoConfig.SHINING_ID);
    }
}
