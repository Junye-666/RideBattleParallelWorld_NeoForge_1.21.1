package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class PWArmorMaterial {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, RideBattleParallelWorlds.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DECADE_MATERIAL =
            register("decade",
                    UtilMap(map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 7);
                    }),
                    16, 2f, 0.1f,
                    ModItems.Decade.WORLDS_FRAGMENT);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KUUGA_MATERIAL =
            register("kuuga",
                    UtilMap(map -> {
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.BODY, 7);
                    }),
                    8, 1f, 0.2f,
                    ModItems.Kuuga.MIGHTY_ELEMENT);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> AGITO_MATERIAL =
            register("agito",
                    UtilMap(map -> {
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    8, 1f, 0.2f,
                    ModItems.Agito.GROUND_ELEMENT);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MIRROR_MATERIAL =
            register("ryuki",
                    UtilMap(map -> {
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    8, 1f, 0.2f,
                    ModItems.Ryuki.MIRROR_FRAGMENT);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RIDER_MATERIAL =
            register("rider",
                    UtilMap(map -> {
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.CHESTPLATE, 7);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.BODY, 8);
                    }),
                    8, 1f, 0.2f,
                    ModItems.Misc.RIDER_INGOT);

    /**
     * 供主类调用：{@code PWArmorMaterial.register(modEventBus)}。
     */
    public static void register(IEventBus bus) {
        ARMOR_MATERIALS.register(bus);
    }

    // ==================== 内部 ====================

    /**
     * 用 lambda 填 EnumMap，避免每个调用点重复 {@code Util.make(new EnumMap<>(...), ...)}。
     */
    private static EnumMap<ArmorItem.Type, Integer> UtilMap(
            java.util.function.Consumer<EnumMap<ArmorItem.Type, Integer>> filler) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        filler.accept(map);
        return map;
    }

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> protection,
            int enchantability,
            float toughness,
            float knockbackResistance,
            Supplier<Item> ingredientItem) {

        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
                RideBattleParallelWorlds.MODID, name);

        return ARMOR_MATERIALS.register(name, () -> {
            EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
            for (ArmorItem.Type type : ArmorItem.Type.values()) {
                typeMap.put(type, protection.getOrDefault(type, 0));
            }
            Supplier<Ingredient> ingredient = () -> Ingredient.of(ingredientItem.get());
            List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));
            Holder<SoundEvent> equipSound = null;
            return new ArmorMaterial(
                    typeMap, enchantability, equipSound,
                    ingredient, layers, toughness, knockbackResistance);
        });
    }
}