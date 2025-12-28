package com.jpigeon.ridebattleparallelworlds.core.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.armor.DecadeBaseArmorItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.armor.*;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RideBattleParallelWorlds.MODID);

    public static final DeferredItem<Item> MIGHTY_ELEMENT = ITEMS.register("kuuga_mighty_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRAGON_ELEMENT = ITEMS.register("kuuga_dragon_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PEGASUS_ELEMENT = ITEMS.register("kuuga_pegasus_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TITAN_ELEMENT = ITEMS.register("kuuga_titan_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RISING_MIGHTY_ELEMENT = ITEMS.register("kuuga_rising_mighty_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RISING_DRAGON_ELEMENT = ITEMS.register("kuuga_rising_dragon_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RISING_PEGASUS_ELEMENT = ITEMS.register("kuuga_rising_pegasus_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RISING_TITAN_ELEMENT = ITEMS.register("kuuga_rising_titan_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AMAZING_MIGHTY_ELEMENT = ITEMS.register("kuuga_amazing_mighty_element", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ULTIMATE_ELEMENT = ITEMS.register("kuuga_ultimate_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WORLDS_FRAGMENT = ITEMS.register("decade_worlds_fragment", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DECADE_BLANK_CARD = ITEMS.register("decade_blank_card", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KAMEN_RIDE_DECADE = ITEMS.register("decade_kamen_ride_decade_card", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KAMEN_RIDE_KUUGA = ITEMS.register("decade_kamen_ride_kuuga_card", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FORM_RIDE_KUUGA_DRAGON = ITEMS.register("decade_form_ride_kuuga_dragon_card", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FORM_RIDE_KUUGA_PEGASUS = ITEMS.register("decade_form_ride_kuuga_pegasus_card", ()
            -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FORM_RIDE_KUUGA_TITAN = ITEMS.register("decade_form_ride_kuuga_titan_card", ()
            -> new Item(new Item.Properties()));


    // 空我
    // 盔甲注册
    // 需要注意: 腰带注册时不能沿用盔甲物品
    public static final DeferredItem<ArcleItem> ARCLE = ITEMS.register("kuuga_arcle", ()
            -> new ArcleItem(PWArmorMaterial.KUUGA_MATERIAL, ArcleItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredItem<KuugaGrowingItem> GROWING_HELMET = ITEMS.register("kuuga_growing_helmet", ()
            -> new KuugaGrowingItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaGrowingItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaGrowingItem> GROWING_CHESTPLATE = ITEMS.register("kuuga_growing_chestplate", ()
            -> new KuugaGrowingItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaGrowingItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaGrowingItem> GROWING_BOOTS = ITEMS.register("kuuga_growing_boots", ()
            -> new KuugaGrowingItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaGrowingItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaMightyItem> MIGHTY_HELMET = ITEMS.register("kuuga_mighty_helmet", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaMightyItem> MIGHTY_CHESTPLATE = ITEMS.register("kuuga_mighty_chestplate", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaMightyItem> MIGHTY_BOOTS = ITEMS.register("kuuga_mighty_boots", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaDragonItem> DRAGON_HELMET = ITEMS.register("kuuga_dragon_helmet", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaDragonItem> DRAGON_CHESTPLATE = ITEMS.register("kuuga_dragon_chestplate", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaDragonItem> DRAGON_BOOTS = ITEMS.register("kuuga_dragon_boots", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaPegasusItem> PEGASUS_HELMET = ITEMS.register("kuuga_pegasus_helmet", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaPegasusItem> PEGASUS_CHESTPLATE = ITEMS.register("kuuga_pegasus_chestplate", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaPegasusItem> PEGASUS_BOOTS = ITEMS.register("kuuga_pegasus_boots", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaTitanItem> TITAN_HELMET = ITEMS.register("kuuga_titan_helmet", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaTitanItem> TITAN_CHESTPLATE = ITEMS.register("kuuga_titan_chestplate", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaTitanItem> TITAN_BOOTS = ITEMS.register("kuuga_titan_boots", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaRisingMightyItem> RISING_MIGHTY_HELMET = ITEMS.register("kuuga_rising_mighty_helmet", ()
            -> new KuugaRisingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingMightyItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaRisingMightyItem> RISING_MIGHTY_CHESTPLATE = ITEMS.register("kuuga_rising_mighty_chestplate", ()
            -> new KuugaRisingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingMightyItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaRisingMightyItem> RISING_MIGHTY_BOOTS = ITEMS.register("kuuga_rising_mighty_boots", ()
            -> new KuugaRisingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingMightyItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaRisingDragonItem> RISING_DRAGON_HELMET = ITEMS.register("kuuga_rising_dragon_helmet", ()
            -> new KuugaRisingDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingDragonItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaRisingDragonItem> RISING_DRAGON_CHESTPLATE = ITEMS.register("kuuga_rising_dragon_chestplate", ()
            -> new KuugaRisingDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingDragonItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaRisingDragonItem> RISING_DRAGON_BOOTS = ITEMS.register("kuuga_rising_dragon_boots", ()
            -> new KuugaRisingDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingDragonItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaRisingPegasusItem> RISING_PEGASUS_HELMET = ITEMS.register("kuuga_rising_pegasus_helmet", ()
            -> new KuugaRisingPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingPegasusItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaRisingPegasusItem> RISING_PEGASUS_CHESTPLATE = ITEMS.register("kuuga_rising_pegasus_chestplate", ()
            -> new KuugaRisingPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingPegasusItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaRisingPegasusItem> RISING_PEGASUS_BOOTS = ITEMS.register("kuuga_rising_pegasus_boots", ()
            -> new KuugaRisingPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingPegasusItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaRisingTitanItem> RISING_TITAN_HELMET = ITEMS.register("kuuga_rising_titan_helmet", ()
            -> new KuugaRisingTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingTitanItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaRisingTitanItem> RISING_TITAN_CHESTPLATE = ITEMS.register("kuuga_rising_titan_chestplate", ()
            -> new KuugaRisingTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingTitanItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaRisingTitanItem> RISING_TITAN_BOOTS = ITEMS.register("kuuga_rising_titan_boots", ()
            -> new KuugaRisingTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaRisingTitanItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaAmazingMightyItem> AMAZING_MIGHTY_HELMET = ITEMS.register("kuuga_amazing_mighty_helmet", ()
            -> new KuugaAmazingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaAmazingMightyItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaAmazingMightyItem> AMAZING_MIGHTY_CHESTPLATE = ITEMS.register("kuuga_amazing_mighty_chestplate", ()
            -> new KuugaAmazingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaAmazingMightyItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaAmazingMightyItem> AMAZING_MIGHTY_BOOTS = ITEMS.register("kuuga_amazing_mighty_boots", ()
            -> new KuugaAmazingMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaAmazingMightyItem.Type.BOOTS, new Item.Properties()));
    public static final DeferredItem<KuugaUltimateItem> ULTIMATE_HELMET = ITEMS.register("kuuga_ultimate_helmet", ()
            -> new KuugaUltimateItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaUltimateItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<KuugaUltimateItem> ULTIMATE_CHESTPLATE = ITEMS.register("kuuga_ultimate_chestplate", ()
            -> new KuugaUltimateItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaUltimateItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<KuugaUltimateItem> ULTIMATE_BOOTS = ITEMS.register("kuuga_ultimate_boots", ()
            -> new KuugaUltimateItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaUltimateItem.Type.BOOTS, new Item.Properties()));

    // 武器注册
    public static final DeferredItem<DragonRodItem> DRAGON_ROD = ITEMS.register("kuuga_dragon_rod", ()
            -> new DragonRodItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2)).rarity(Rarity.RARE)));
    public static final DeferredItem<PegasusBowgunItem> PEGASUS_BOWGUN = ITEMS.register("kuuga_pegasus_bowgun", ()
            -> new PegasusBowgunItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<TitanSwordItem> TITAN_SWORD = ITEMS.register("kuuga_titan_sword", ()
            -> new TitanSwordItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 6, 0)).rarity(Rarity.RARE)));
    public static final DeferredItem<RisingDragonRodItem> RISING_DRAGON_ROD = ITEMS.register("kuuga_rising_dragon_rod", ()
            -> new RisingDragonRodItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, -2)).rarity(Rarity.EPIC)));
    public static final DeferredItem<RisingPegasusBowgunItem> RISING_PEGASUS_BOWGUN = ITEMS.register("kuuga_rising_pegasus_bowgun", ()
            -> new RisingPegasusBowgunItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredItem<RisingTitanSwordItem> RISING_TITAN_SWORD = ITEMS.register("kuuga_rising_titan_sword", ()
            -> new RisingTitanSwordItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 6, 0)).rarity((Rarity.EPIC))));

    // Decade
    // 盔甲
    public static final DeferredItem<DecaDriverItem> DECA_DRIVER = ITEMS.register("decade_deca_driver", ()
            -> new DecaDriverItem(PWArmorMaterial.DECADE_MATERIAL, DecaDriverItem.Type.LEGGINGS, new Item.Properties()));

    public static final DeferredItem<DecadeBaseArmorItem> DECADE_HELMET = ITEMS.register("decade_base_helmet", ()
            -> new DecadeBaseArmorItem(PWArmorMaterial.DECADE_MATERIAL, DecadeBaseArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredItem<DecadeBaseArmorItem> DECADE_CHESTPLATE = ITEMS.register("decade_base_chestplate", ()
            -> new DecadeBaseArmorItem(PWArmorMaterial.DECADE_MATERIAL, DecadeBaseArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredItem<DecadeBaseArmorItem> DECADE_BOOTS = ITEMS.register("decade_base_boots", ()
            -> new DecadeBaseArmorItem(PWArmorMaterial.DECADE_MATERIAL, DecadeBaseArmorItem.Type.BOOTS, new Item.Properties()));




    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
