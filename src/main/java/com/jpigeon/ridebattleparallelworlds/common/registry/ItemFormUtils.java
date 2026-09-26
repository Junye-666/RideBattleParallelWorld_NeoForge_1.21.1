package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Agito.*;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.*;

public class ItemFormUtils {
    public static final Map<Item, ResourceLocation> RIDER_ITEM_FORM_MAP = new HashMap<>();

    static {
        RIDER_ITEM_FORM_MAP.put(MIGHTY_ELEMENT.get(), KuugaConfig.MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(DRAGON_ELEMENT.get(), KuugaConfig.DRAGON_ID);
        RIDER_ITEM_FORM_MAP.put(PEGASUS_ELEMENT.get(), KuugaConfig.PEGASUS_ID);
        RIDER_ITEM_FORM_MAP.put(TITAN_ELEMENT.get(), KuugaConfig.TITAN_ID);
        RIDER_ITEM_FORM_MAP.put(RISING_MIGHTY_ELEMENT.get(), KuugaConfig.RISING_MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(RISING_DRAGON_ELEMENT.get(), KuugaConfig.RISING_DRAGON_ID);
        RIDER_ITEM_FORM_MAP.put(RISING_PEGASUS_ELEMENT.get(), KuugaConfig.RISING_PEGASUS_ID);
        RIDER_ITEM_FORM_MAP.put(RISING_TITAN_ELEMENT.get(), KuugaConfig.RISING_TITAN_ID);
        RIDER_ITEM_FORM_MAP.put(AMAZING_MIGHTY_ELEMENT.get(), KuugaConfig.AMAZING_MIGHTY_ID);
        RIDER_ITEM_FORM_MAP.put(ULTIMATE_ELEMENT.get(), KuugaConfig.ULTIMATE_ID);

        RIDER_ITEM_FORM_MAP.put(GROUND_ELEMENT.get(), AgitoConfig.GROUND_ID);
        RIDER_ITEM_FORM_MAP.put(FLAME_ELEMENT.get(), AgitoConfig.FLAME_ID);
        RIDER_ITEM_FORM_MAP.put(STORM_ELEMENT.get(), AgitoConfig.STORM_ID);
        RIDER_ITEM_FORM_MAP.put(TRINITY_ELEMENT.get(), AgitoConfig.TRINITY_ID);
        RIDER_ITEM_FORM_MAP.put(BURNING_ELEMENT.get(), AgitoConfig.BURNING_ID);
        RIDER_ITEM_FORM_MAP.put(SHINING_ELEMENT.get(), AgitoConfig.SHINING_ID);
    }
}
