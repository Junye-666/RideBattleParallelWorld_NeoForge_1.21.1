package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class PWCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RideBattleParallelWorlds.MODID);


    public static final Supplier<CreativeModeTab> KUUGA_ITEMS_TAB = CREATIVE_MODE_TAB.register("kuuga_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.Kuuga.MIGHTY_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.kuuga_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.Kuuga.ARCLE);
                        output.accept(ModItems.Kuuga.MIGHTY_ELEMENT);
                        output.accept(ModItems.Kuuga.DRAGON_ELEMENT);
                        output.accept(ModItems.Kuuga.PEGASUS_ELEMENT);
                        output.accept(ModItems.Kuuga.TITAN_ELEMENT);
                        output.accept(ModItems.Kuuga.RISING_MIGHTY_ELEMENT);
                        output.accept(ModItems.Kuuga.RISING_DRAGON_ELEMENT);
                        output.accept(ModItems.Kuuga.RISING_PEGASUS_ELEMENT);
                        output.accept(ModItems.Kuuga.RISING_TITAN_ELEMENT);
                        output.accept(ModItems.Kuuga.AMAZING_MIGHTY_ELEMENT);
                        output.accept(ModItems.Kuuga.ULTIMATE_ELEMENT);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> AGITO_ITEMS_TAB = CREATIVE_MODE_TAB.register("agito_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.Agito.GROUND_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.agito_items"))
                    .withTabsBefore(id("kuuga_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.Agito.ALTER_RING);
                        output.accept(ModItems.Agito.GROUND_ELEMENT);
                        output.accept(ModItems.Agito.FLAME_ELEMENT);
                        output.accept(ModItems.Agito.STORM_ELEMENT);
                        output.accept(ModItems.Agito.TRINITY_ELEMENT);
                        output.accept(ModItems.Agito.BURNING_ELEMENT);
                        output.accept(ModItems.Agito.FLAME_SABER);
                        output.accept(ModItems.Agito.STORM_HALBERD);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> DECADE_ITEMS_TAB = CREATIVE_MODE_TAB.register("decade_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.Decade.DECADE_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.decade_items"))
                    .withTabsBefore(id("agito_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.Decade.DECA_DRIVER);
                        output.accept(ModItems.Decade.WORLDS_FRAGMENT);
                        output.accept(ModItems.Decade.DECADE_BLANK_CARD);
                        output.accept(ModItems.Decade.KAMEN_RIDE_DECADE);
                        output.accept(ModItems.Decade.KAMEN_RIDE_KUUGA);
                        output.accept(ModItems.Decade.FORM_RIDE_KUUGA_DRAGON);
                        output.accept(ModItems.Decade.FORM_RIDE_KUUGA_PEGASUS);
                        output.accept(ModItems.Decade.FORM_RIDE_KUUGA_TITAN);
                        output.accept(ModItems.Decade.KAMEN_RIDE_AGITO);
                        output.accept(ModItems.Decade.FORM_RIDE_AGITO_FLAME);
                        output.accept(ModItems.Decade.FORM_RIDE_AGITO_STORM);
                        output.accept(ModItems.Decade.FORM_RIDE_AGITO_BURNING);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
