package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.config.TriggerType;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds.id;

public class DecadeConfig {
    public static final ResourceLocation DECA_CARD = id("decade_card_slot");
    public static final ResourceLocation DECADE_BASE_ID = id("decade_base");

    private static ResourceLocation ride(ResourceLocation original) {
        return ResourceLocation.fromNamespaceAndPath(original.getNamespace(), "decade_" + original.getPath());
    }

    public static List<Item> getRideCards() {
        return BuiltInRegistries.ITEM.entrySet().stream()
                .filter(entry -> {
                    ResourceLocation id = entry.getKey().location();
                    return id.getPath().contains("_ride") && id.getPath().endsWith("_card");
                })
                .map(Map.Entry::getValue)
                .toList();
    }

    public static final RiderConfig DECADE = new RiderConfig(RiderIds.DECADE_ID)
            .setMainDriverItem(ModItems.DECA_DRIVER.get())
            .addMainDriverSlot(
                    DECA_CARD,
                    getRideCards(),
                    true,
                    true
            );

    public static final FormConfig DECADE_BASE = new FormConfig(DECADE_BASE_ID)
            .setArmor(ModItems.DECADE_HELMET.get(),
                    ModItems.DECADE_CHESTPLATE.get(),
                    null,
                    ModItems.DECADE_BOOTS.get())
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .addRequiredItem(DECA_CARD, ModItems.KAMEN_RIDE_DECADE.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);

    public static final FormConfig DECADE_KUUGA_MIGHTY = KuugaConfig.KUUGA_MIGHTY_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.MIGHTY_ID))
            .addRequiredItem(DECA_CARD, ModItems.KAMEN_RIDE_KUUGA.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);

    public static final FormConfig DECADE_KUUGA_DRAGON = KuugaConfig.KUUGA_DRAGON_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.DRAGON_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_DRAGON.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.DRAGON_ROD.get());

    public static final FormConfig DECADE_KUUGA_PEGASUS = KuugaConfig.KUUGA_PEGASUS_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.PEGASUS_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_PEGASUS.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.PEGASUS_BOWGUN.get());

    public static final FormConfig DECADE_KUUGA_TITAN = KuugaConfig.KUUGA_TITAN_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.TITAN_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_TITAN.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.TITAN_SWORD.get());

    public static final FormConfig DECADE_AGITO_GROUND = AgitoConfig.AGITO_GROUND_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.GROUND_ID))
            .addRequiredItem(DECA_CARD, ModItems.KAMEN_RIDE_AGITO.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);

    public static final FormConfig DECADE_AGITO_FLAME = AgitoConfig.AGITO_FLAME_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.FLAME_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_AGITO_FLAME.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.FLAME_SABER.get());

    public static final FormConfig DECADE_AGITO_STORM = AgitoConfig.AGITO_STORM_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.STORM_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_AGITO_STORM.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.STORM_HALBERD.get());

    public static final FormConfig DECADE_AGITO_BURNING = AgitoConfig.AGITO_BURNING_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.BURNING_ID))
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_AGITO_BURNING.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);


    private static void registerDecade() {
        DECADE.addForm(DECADE_BASE);

        DECADE.addForm(DECADE_KUUGA_MIGHTY);
        DECADE.addForm(DECADE_KUUGA_DRAGON);
        DECADE.addForm(DECADE_KUUGA_PEGASUS);
        DECADE.addForm(DECADE_KUUGA_TITAN);

        DECADE.addForm(DECADE_AGITO_GROUND);
        DECADE.addForm(DECADE_AGITO_FLAME);
        DECADE.addForm(DECADE_AGITO_STORM);
        DECADE.addForm(DECADE_AGITO_BURNING);

        RiderRegistry.registerRider(DECADE);
    }


    public static void init() {
        registerDecade();
    }
}
