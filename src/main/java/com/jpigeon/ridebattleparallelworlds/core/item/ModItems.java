package com.jpigeon.ridebattleparallelworlds.core.item;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaMightyArmorItem;
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
    public static final DeferredItem<KuugaMightyArmorItem> KUUGA_HELMET = ITEMS.register("kuuga_helmet", ()
            -> new KuugaMightyArmorItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyArmorItem> KUUGA_CHESTPLATE = ITEMS.register("kuuga_chestplate", ()
            -> new KuugaMightyArmorItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<KuugaMightyArmorItem> KUUGA_BOOTS = ITEMS.register("kuuga_boots", ()
            -> new KuugaMightyArmorItem(PWArmorMaterial.KUUGA_MATERIAL, KuugaMightyArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    // 需要注意: 腰带注册时不能沿用盔甲物品
    public static final DeferredItem<ArcleItem> ARCLE = ITEMS.register("arcle", ()
            -> new ArcleItem(PWArmorMaterial.KUUGA_MATERIAL, ArcleItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));


    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
