package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattlelib.server.event.RiderRegisterEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Agito.*;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig.*;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public final class ItemFormRegistry {

    private ItemFormRegistry() {
    }

    private static final Map<Item, ResourceLocation> ITEM_TO_FORM = new HashMap<>();
    private static final Map<ResourceLocation, Item> FORM_TO_ITEM = new HashMap<>();

    @SubscribeEvent
    public static void onRiderRegister(RiderRegisterEvent event) {
        ResourceLocation riderId = event.getRiderId();
        if (RiderIds.KUUGA_ID.equals(riderId)) registerKuuga();
        else if (RiderIds.AGITO_ID.equals(riderId)) registerAgito();
    }

    private static void registerKuuga() {
        bind(MIGHTY_ELEMENT.get(), MIGHTY_ID);
        bind(DRAGON_ELEMENT.get(), DRAGON_ID);
        bind(PEGASUS_ELEMENT.get(), PEGASUS_ID);
        bind(TITAN_ELEMENT.get(), TITAN_ID);
        bind(RISING_MIGHTY_ELEMENT.get(), RISING_MIGHTY_ID);
        bind(RISING_DRAGON_ELEMENT.get(), RISING_DRAGON_ID);
        bind(RISING_PEGASUS_ELEMENT.get(), RISING_PEGASUS_ID);
        bind(RISING_TITAN_ELEMENT.get(), RISING_TITAN_ID);
        bind(AMAZING_MIGHTY_ELEMENT.get(), AMAZING_MIGHTY_ID);
        bind(ULTIMATE_ELEMENT.get(), ULTIMATE_ID);
    }

    private static void registerAgito() {
        bind(GROUND_ELEMENT.get(), GROUND_ID);
        bind(FLAME_ELEMENT.get(), FLAME_ID);
        bind(STORM_ELEMENT.get(), STORM_ID);
        bind(TRINITY_ELEMENT.get(), TRINITY_ID);
        bind(BURNING_ELEMENT.get(), BURNING_ID);
        bind(SHINING_ELEMENT.get(), SHINING_ID);
    }

    private static void bind(Item item, ResourceLocation formId) {
        ITEM_TO_FORM.put(item, formId);
        FORM_TO_ITEM.put(formId, item);
    }

    public static @Nullable ResourceLocation getFormForItem(Item item) {
        return ITEM_TO_FORM.get(item);
    }

    public static @Nullable Item getItemForForm(ResourceLocation formId) {
        return formId == null ? null : FORM_TO_ITEM.get(formId);
    }
}
