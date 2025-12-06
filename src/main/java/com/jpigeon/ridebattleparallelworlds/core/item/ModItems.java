package com.jpigeon.ridebattleparallelworlds.core.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.*;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RideBattleParallelWorlds.MODID);

    public static final DeferredItem<Item> CHOGODAI_ELEMENT = ITEMS.register("kuuga/chogodai_element",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MIGHTY_ELEMENT = ITEMS.register("kuuga/mighty_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRAGON_ELEMENT = ITEMS.register("kuuga/dragon_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PEGASUS_ELEMENT = ITEMS.register("kuuga/pegasus_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TITAN_ELEMENT = ITEMS.register("kuuga/titan_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WORLDS_FRAGMENT =ITEMS.register("decade/worlds_fragment",
            () -> new Item(new Item.Properties()));

    // 盔甲注册
    public static final DeferredItem<KuugaMightyItem> MIGHTY_HELMET = ITEMS.register("kuuga/armor/kuuga_mighty_helmet", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyItem> MIGHTY_CHESTPLATE = ITEMS.register("kuuga/armor/kuuga_mighty_chestplate", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyItem> MIGHTY_BOOTS = ITEMS.register("kuuga/armor/kuuga_mighty_boots", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_HELMET = ITEMS.register("kuuga/armor/kuuga_dragon_helmet", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_CHESTPLATE = ITEMS.register("kuuga/armor/kuuga_dragon_chestplate", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_BOOTS = ITEMS.register("kuuga/armor/kuuga_dragon_boots", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaDragonItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaPegasusItem> PEGASUS_HELMET = ITEMS.register("kuuga/armor/kuuga_pegasus_helmet", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaPegasusItem> PEGASUS_CHESTPLATE = ITEMS.register("kuuga/armor/kuuga_pegasus_chestplate", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaPegasusItem> PEGASUS_BOOTS = ITEMS.register("kuuga/armor/kuuga_pegasus_boots", ()
            -> new KuugaPegasusItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaPegasusItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaTitanItem> TITAN_HELMET = ITEMS.register("kuuga/armor/kuuga_titan_helmet", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaTitanItem> TITAN_CHESTPLATE = ITEMS.register("kuuga/armor/kuuga_titan_chestplate", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaTitanItem> TITAN_BOOTS = ITEMS.register("kuuga/armor/kuuga_titan_boots", ()
            -> new KuugaTitanItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaTitanItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // 需要注意: 腰带注册时不能沿用盔甲物品
    public static final DeferredItem<ArcleItem> ARCLE = ITEMS.register("kuuga/arcle", ()
            -> new ArcleItem(PWArmorMaterial.KUUGA_MATERIAL, ArcleItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));


    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
