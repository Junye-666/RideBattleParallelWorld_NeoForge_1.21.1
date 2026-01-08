package com.jpigeon.ridebattleparallelworlds.core.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class PWCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RideBattleParallelWorlds.MODID);


    public static final Supplier<CreativeModeTab> KUUGA_ITEMS_TAB = CREATIVE_MODE_TAB.register("kuuga_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MIGHTY_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.kuuga_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ARCLE);
                        output.accept(ModItems.MIGHTY_ELEMENT);
                        output.accept(ModItems.DRAGON_ELEMENT);
                        output.accept(ModItems.PEGASUS_ELEMENT);
                        output.accept(ModItems.TITAN_ELEMENT);
                        output.accept(ModItems.RISING_MIGHTY_ELEMENT);
                        output.accept(ModItems.RISING_DRAGON_ELEMENT);
                        output.accept(ModItems.RISING_PEGASUS_ELEMENT);
                        output.accept(ModItems.RISING_TITAN_ELEMENT);
                        output.accept(ModItems.AMAZING_MIGHTY_ELEMENT);
                        output.accept(ModItems.ULTIMATE_ELEMENT);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> AGITO_ITEMS_TAB = CREATIVE_MODE_TAB.register("agito_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GROUND_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.agito_items"))
                    .withTabsBefore(fromString("kuuga_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ALTER_RING);
                        output.accept(ModItems.GROUND_ELEMENT);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> DECADE_ITEMS_TAB = CREATIVE_MODE_TAB.register("decade_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DECADE_HELMET.get()))
                    .title(Component.translatable("creativeTab.ridebattleparallelworlds.decade_items"))
                    .withTabsBefore(fromString("agito_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DECA_DRIVER);
                        output.accept(ModItems.WORLDS_FRAGMENT);
                        output.accept(ModItems.DECADE_BLANK_CARD);
                        output.accept(ModItems.KAMEN_RIDE_DECADE);
                        output.accept(ModItems.KAMEN_RIDE_KUUGA);
                        output.accept(ModItems.FORM_RIDE_KUUGA_DRAGON);
                        output.accept(ModItems.FORM_RIDE_KUUGA_PEGASUS);
                        output.accept(ModItems.FORM_RIDE_KUUGA_TITAN);
                        output.accept(ModItems.KAMEN_RIDE_AGITO);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
