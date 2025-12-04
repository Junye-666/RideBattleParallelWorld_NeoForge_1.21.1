package com.jpigeon.ridebattleparallelworlds.core.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaDragonItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaMightyItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RideBattleParallelWorlds.MODID);

    public static final DeferredItem<Item> CHOGODAI_ELEMENT = ITEMS.register("chogodai_element",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MIGHTY_ELEMENT = ITEMS.register("mighty_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRAGON_ELEMENT = ITEMS.register("dragon_element", ()
            -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WORLDS_FRAGMENT =ITEMS.register("worlds_fragment",
            () -> new Item(new Item.Properties()));

    // 盔甲注册
    public static final DeferredItem<KuugaMightyItem> MIGHTY_HELMET = ITEMS.register("kuuga_mighty_helmet", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyItem> MIGHTY_CHESTPLATE = ITEMS.register("kuuga_mighty_chestplate", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyItem> MIGHTY_BOOTS = ITEMS.register("kuuga_mighty_boots", ()
            -> new KuugaMightyItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_HELMET = ITEMS.register("kuuga_dragon_helmet", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_CHESTPLATE = ITEMS.register("kuuga_dragon_chestplate", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaDragonItem> DRAGON_BOOTS = ITEMS.register("kuuga_dragon_boots", ()
            -> new KuugaDragonItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // 需要注意: 腰带注册时不能沿用盔甲物品
    public static final DeferredItem<ArcleItem> ARCLE = ITEMS.register("arcle", ()
            -> new ArcleItem(PWArmorMaterial.KUUGA_MATERIAL, ArcleItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));


    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
