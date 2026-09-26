package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.StormHalberdItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.armor.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item.*;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.armor.VBuckleItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item.MirrorDeckItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item.VentCardItem;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RideBattleParallelWorlds.MODID);

    public static class Kuuga {
        private Kuuga() {
        }

        private static final String MOD = RideBattleParallelWorlds.MODID;

        /**
         * 通用形态盔甲三件套工厂
         */
        private static DeferredItem<ParallelRiderArmor> armor(String form, ArmorItem.Type type) {
            String name = "kuuga_" + form + "_" + type.getName();
            return ITEMS.register(name, () ->
                    new ParallelRiderArmor(
                            MOD, "kuuga", form,
                            PWArmorMaterial.KUUGA_MATERIAL,
                            type,
                            new Item.Properties(), false));
        }

        // 物品
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

        // 盔甲
        public static final DeferredItem<ArcleItem> ARCLE = ITEMS.register("kuuga_arcle", ()
                -> new ArcleItem(PWArmorMaterial.KUUGA_MATERIAL, ArcleItem.Type.LEGGINGS, new Item.Properties()));

        public static final DeferredItem<ParallelRiderArmor> GROWING_HELMET = armor("growing", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> GROWING_CHESTPLATE = armor("growing", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> GROWING_BOOTS = armor("growing", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> MIGHTY_HELMET = armor("mighty", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> MIGHTY_CHESTPLATE = armor("mighty", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> MIGHTY_BOOTS = armor("mighty", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> DRAGON_HELMET = armor("dragon", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> DRAGON_CHESTPLATE = armor("dragon", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> DRAGON_BOOTS = armor("dragon", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> PEGASUS_HELMET = armor("pegasus", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> PEGASUS_CHESTPLATE = armor("pegasus", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> PEGASUS_BOOTS = armor("pegasus", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> TITAN_HELMET = armor("titan", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> TITAN_CHESTPLATE = armor("titan", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> TITAN_BOOTS = armor("titan", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> RISING_MIGHTY_HELMET = armor("rising_mighty", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> RISING_MIGHTY_CHESTPLATE = armor("rising_mighty", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> RISING_MIGHTY_BOOTS = armor("rising_mighty", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> RISING_DRAGON_HELMET = armor("rising_dragon", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> RISING_DRAGON_CHESTPLATE = armor("rising_dragon", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> RISING_DRAGON_BOOTS = armor("rising_dragon", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> RISING_PEGASUS_HELMET = armor("rising_pegasus", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> RISING_PEGASUS_CHESTPLATE = armor("rising_pegasus", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> RISING_PEGASUS_BOOTS = armor("rising_pegasus", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> RISING_TITAN_HELMET = armor("rising_titan", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> RISING_TITAN_CHESTPLATE = armor("rising_titan", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> RISING_TITAN_BOOTS = armor("rising_titan", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> AMAZING_MIGHTY_HELMET = armor("amazing_mighty", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> AMAZING_MIGHTY_CHESTPLATE = armor("amazing_mighty", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> AMAZING_MIGHTY_BOOTS = armor("amazing_mighty", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> ULTIMATE_HELMET = armor("ultimate", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> ULTIMATE_CHESTPLATE = armor("ultimate", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> ULTIMATE_BOOTS = armor("ultimate", ArmorItem.Type.BOOTS);

        // 武器
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

        public static void init() {
        }
    }

    public static class Agito {
        private Agito() {
        }

        private static final String MOD = RideBattleParallelWorlds.MODID;

        /**
         * 通用形态盔甲三件套工厂
         */
        private static DeferredItem<ParallelRiderArmor> armor(String form, ArmorItem.Type type) {
            String name = "agito_" + form + "_" + type.getName();
            return ITEMS.register(name, () ->
                    new ParallelRiderArmor(
                            MOD, "agito", form,
                            PWArmorMaterial.AGITO_MATERIAL,
                            type,
                            new Item.Properties(), false));
        }

        public static final DeferredItem<Item> GROUND_ELEMENT = ITEMS.register("agito_ground_element", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> FLAME_ELEMENT = ITEMS.register("agito_flame_element", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> STORM_ELEMENT = ITEMS.register("agito_storm_element", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> TRINITY_ELEMENT = ITEMS.register("agito_trinity_element", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> BURNING_ELEMENT = ITEMS.register("agito_burning_element", ()
                -> new Item(new Item.Properties()));
        // TODO: 闪耀形态
        public static final DeferredItem<Item> SHINING_ELEMENT = ITEMS.register("agito_shining_element", ()
                -> new Item(new Item.Properties()));

        // 盔甲
        public static final DeferredItem<AlterRingItem> ALTER_RING = ITEMS.register("agito_alter_ring", ()
                -> new AlterRingItem(PWArmorMaterial.AGITO_MATERIAL, AlterRingItem.Type.LEGGINGS, new Item.Properties()));

        public static final DeferredItem<AgitoGroundItem> GROUND_HELMET = ITEMS.register("agito_ground_helmet", ()
                -> new AgitoGroundItem(PWArmorMaterial.AGITO_MATERIAL, AgitoGroundItem.Type.HELMET, new Item.Properties()));
        public static final DeferredItem<AgitoGroundItem> GROUND_CHESTPLATE = ITEMS.register("agito_ground_chestplate", ()
                -> new AgitoGroundItem(PWArmorMaterial.AGITO_MATERIAL, AgitoGroundItem.Type.CHESTPLATE, new Item.Properties()));
        public static final DeferredItem<AgitoGroundItem> GROUND_BOOTS = ITEMS.register("agito_ground_boots", ()
                -> new AgitoGroundItem(PWArmorMaterial.AGITO_MATERIAL, AgitoGroundItem.Type.BOOTS, new Item.Properties()));

        public static final DeferredItem<ParallelRiderArmor> FLAME_HELMET = armor("flame", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> FLAME_CHESTPLATE = armor("flame", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> FLAME_BOOTS = armor("flame", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> STORM_HELMET = armor("storm", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> STORM_CHESTPLATE = armor("storm", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> STORM_BOOTS = armor("storm", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> TRINITY_HELMET = armor("trinity", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> TRINITY_CHESTPLATE = armor("trinity", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> TRINITY_BOOTS = armor("trinity", ArmorItem.Type.BOOTS);

        public static final DeferredItem<ParallelRiderArmor> BURNING_HELMET = armor("burning", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> BURNING_CHESTPLATE = armor("burning", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> BURNING_BOOTS = armor("burning", ArmorItem.Type.BOOTS);

        // 武器
        public static final DeferredItem<FlameSaberItem> FLAME_SABER = ITEMS.register("agito_flame_saber", ()
                -> new FlameSaberItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, 0)).rarity(Rarity.RARE)));
        public static final DeferredItem<StormHalberdItem> STORM_HALBERD = ITEMS.register("agito_storm_halberd", ()
                -> new StormHalberdItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 2, -0.5f)).rarity(Rarity.RARE)));
        public static final DeferredItem<ShiningCaliburItem> SHINING_CALIBUR = ITEMS.register("agito_shining_calibur", ()
                -> new ShiningCaliburItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 4, 0.5f)).rarity(Rarity.RARE)));

        public static void init() {
        }
    }

    public static class Ryuki {
        private Ryuki() {
        }

        private static final String MOD = RideBattleParallelWorlds.MODID;

        /**
         * 通用形态盔甲三件套工厂
         */
        private static DeferredItem<ParallelRiderArmor> armor(String form, ArmorItem.Type type) {
            String name = "mirror_" + form + "_" + type.getName();
            return ITEMS.register(name, () ->
                    new ParallelRiderArmor(
                            MOD, "mirror", form,
                            PWArmorMaterial.DECADE_MATERIAL,
                            type,
                            new Item.Properties(), false));
        }

        // TODO: 合成配方
        public static final DeferredItem<Item> MIRROR_FRAGMENT = ITEMS.register("mirror_fragment", ()
                -> new Item(new Item.Properties()));

        public static final DeferredItem<Item> BLANK_DECK = ITEMS.register("mirror_blank_deck", ()
                -> new MirrorDeckItem(() -> MirrorConfig.MIRROR_BLANK, new Item.Properties()));

        public static final DeferredItem<Item> RYUKI_DECK = ITEMS.register("mirror_ryuki_deck", ()
                -> new MirrorDeckItem(() -> MirrorConfig.RYUKI_BASE_ID, new Item.Properties()));

        public static final DeferredItem<VBuckleItem> V_BUCKLE = ITEMS.register("mirror_v-buckle", ()
                -> new VBuckleItem(PWArmorMaterial.MIRROR_MATERIAL, VBuckleItem.Type.LEGGINGS, new Item.Properties()));

        // 盔甲
        public static final DeferredItem<ParallelRiderArmor> RYUKI_BASE_HELMET = armor("ryuki", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> RYUKI_BASE_CHESTPLATE = armor("ryuki", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> RYUKI_BASE_BOOTS = armor("ryuki", ArmorItem.Type.BOOTS);

        // 降临卡（贴图用的物品本体，玩家通过抽卡获得，右键使用）
        public static final DeferredItem<VentCardItem> RYUKI_ADVENT_CARD = ITEMS.register("ryuki_advent_card",
                () -> new VentCardItem("ad", new Item.Properties()));
        public static final DeferredItem<VentCardItem> RYUKI_SWORD_VENT_CARD = ITEMS.register("ryuki_sword_vent_card",
                () -> new VentCardItem("sword", new Item.Properties()));
        public static final DeferredItem<VentCardItem> RYUKI_STRIKE_VENT_CARD = ITEMS.register("ryuki_strike_vent_card",
                () -> new VentCardItem("strike", new Item.Properties()));
        public static final DeferredItem<VentCardItem> RYUKI_GUARD_VENT_CARD = ITEMS.register("ryuki_guard_vent_card",
                () -> new VentCardItem("guard", new Item.Properties()));
        public static final DeferredItem<VentCardItem> RYUKI_FINAL_VENT_CARD = ITEMS.register("ryuki_final_vent_card",
                () -> new VentCardItem("final", new Item.Properties()));

        public static void init() {
        }
    }

    public static class Decade {
        private Decade() {
        }

        private static final String MOD = RideBattleParallelWorlds.MODID;

        /**
         * 通用形态盔甲三件套工厂
         */
        private static DeferredItem<ParallelRiderArmor> armor(String form, ArmorItem.Type type) {
            String name = "decade_" + form + "_" + type.getName();
            return ITEMS.register(name, () ->
                    new ParallelRiderArmor(
                            MOD, "decade", form,
                            PWArmorMaterial.DECADE_MATERIAL,
                            type,
                            new Item.Properties(), false));
        }

        public static final DeferredItem<Item> WORLDS_FRAGMENT = ITEMS.register("decade_worlds_fragment", ()
                -> new Item(new Item.Properties()));
        // TODO: 合成配方
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
        public static final DeferredItem<Item> KAMEN_RIDE_AGITO = ITEMS.register("decade_kamen_ride_agito_card", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> FORM_RIDE_AGITO_FLAME = ITEMS.register("decade_form_ride_agito_flame_card", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> FORM_RIDE_AGITO_STORM = ITEMS.register("decade_form_ride_agito_storm_card", ()
                -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> FORM_RIDE_AGITO_BURNING = ITEMS.register("decade_form_ride_agito_burning_card", ()
                -> new Item(new Item.Properties()));

        // 盔甲
        public static final DeferredItem<DecaDriverItem> DECA_DRIVER = ITEMS.register("decade_deca_driver", ()
                -> new DecaDriverItem(PWArmorMaterial.DECADE_MATERIAL, DecaDriverItem.Type.LEGGINGS, new Item.Properties()));

        public static final DeferredItem<ParallelRiderArmor> DECADE_HELMET = armor("base", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> DECADE_CHESTPLATE = armor("base", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> DECADE_BOOTS = armor("base", ArmorItem.Type.BOOTS);

        public static void init() {
        }
    }

    public static class Misc {
        private Misc() {
        }

        private static final String MOD = RideBattleParallelWorlds.MODID;

        /**
         * 通用形态盔甲三件套工厂
         */
        private static DeferredItem<ParallelRiderArmor> armor(String id, String form, ArmorItem.Type type) {
            String name = id + "_" + form + "_" + type.getName();
            return ITEMS.register(name, () ->
                    new ParallelRiderArmor(
                            MOD, id, form,
                            PWArmorMaterial.RIDER_MATERIAL,
                            type,
                            new Item.Properties(), false));
        }

        public static final DeferredItem<Item> RIDER_INGOT = ITEMS.register("rider_ingot", ()
                -> new Item(new Item.Properties()));

        // 修卡
        public static final DeferredItem<ParallelRiderArmor> SHOCKER_HELMET = armor("shocker", "combatman", ArmorItem.Type.HELMET);
        public static final DeferredItem<ParallelRiderArmor> SHOCKER_CHESTPLATE = armor("shocker", "combatman", ArmorItem.Type.CHESTPLATE);
        public static final DeferredItem<ParallelRiderArmor> SHOCKER_LEGGINGS = armor("shocker", "combatman", ArmorItem.Type.LEGGINGS);
        public static final DeferredItem<ParallelRiderArmor> SHOCKER_BOOTS = armor("shocker", "combatman", ArmorItem.Type.BOOTS);

        public static void init() {
        }
    }

    public static void register(IEventBus modEventBus) {
        Kuuga.init();
        Agito.init();
        Ryuki.init();
        Decade.init();
        Misc.init();

        ITEMS.register(modEventBus);
    }
}
