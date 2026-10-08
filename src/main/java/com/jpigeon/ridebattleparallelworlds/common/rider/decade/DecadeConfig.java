package com.jpigeon.ridebattleparallelworlds.common.rider.decade;

import com.jpigeon.ridebattlelib.common.api.builder.FormBuilder;
import com.jpigeon.ridebattlelib.common.api.builder.RiderBuilder;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.config.TriggerType;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Agito;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Decade.*;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga;
import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

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

    public static final FormConfig DECADE_BASE = FormBuilder.create(DECADE_BASE_ID)
            .armor(
                    DECADE_HELMET.get(),
                    DECADE_CHESTPLATE.get(),
                    null,
                    DECADE_BOOTS.get())
            .effect(MobEffects.JUMP, -1, 0, true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .requiredItem(DECA_CARD, KAMEN_RIDE_DECADE.get())
            .shouldPause(true)
            .triggerType(TriggerType.AUTO)
            .build();

    public static final FormConfig DECADE_KUUGA_MIGHTY = KuugaConfig.KUUGA_MIGHTY_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.MIGHTY_ID))
            .addRequiredItem(DECA_CARD, KAMEN_RIDE_KUUGA.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);

    public static final FormConfig DECADE_KUUGA_DRAGON = KuugaConfig.KUUGA_DRAGON_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.DRAGON_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_KUUGA_DRAGON.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(Kuuga.DRAGON_ROD.get());

    public static final FormConfig DECADE_KUUGA_PEGASUS = KuugaConfig.KUUGA_PEGASUS_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.PEGASUS_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_KUUGA_PEGASUS.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(Kuuga.PEGASUS_BOWGUN.get());

    public static final FormConfig DECADE_KUUGA_TITAN = KuugaConfig.KUUGA_TITAN_FORM.copyWithoutItemsAndSkills(ride(KuugaConfig.TITAN_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_KUUGA_TITAN.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(Kuuga.TITAN_SWORD.get());

    public static final FormConfig DECADE_AGITO_GROUND = AgitoConfig.AGITO_GROUND_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.GROUND_ID))
            .addRequiredItem(DECA_CARD, KAMEN_RIDE_AGITO.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);

    public static final FormConfig DECADE_AGITO_FLAME = AgitoConfig.AGITO_FLAME_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.FLAME_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_AGITO_FLAME.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(Agito.FLAME_SABER.get());

    public static final FormConfig DECADE_AGITO_STORM = AgitoConfig.AGITO_STORM_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.STORM_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_AGITO_STORM.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(Agito.STORM_HALBERD.get());

    public static final FormConfig DECADE_AGITO_BURNING = AgitoConfig.AGITO_BURNING_FORM.copyWithoutItemsAndSkills(ride(AgitoConfig.BURNING_ID))
            .addRequiredItem(DECA_CARD, FORM_RIDE_AGITO_BURNING.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO);


    public static final RiderConfig DECADE = RiderBuilder.create(RiderIds.DECADE_ID)
            .driver(DECA_DRIVER.get())
            .slot(
                    DECA_CARD,
                    getRideCards(),
                    true,
                    true
            )
            .form(DECADE_BASE)

            .form(DECADE_KUUGA_MIGHTY)
            .form(DECADE_KUUGA_DRAGON)
            .form(DECADE_KUUGA_PEGASUS)
            .form(DECADE_KUUGA_TITAN)

            .form(DECADE_AGITO_GROUND)
            .form(DECADE_AGITO_FLAME)
            .form(DECADE_AGITO_STORM)
            .form(DECADE_AGITO_BURNING)

            .baseForm(DECADE_BASE.getFormId())
            .build();

    public static void init() {
        RiderRegistry.registerRider(DECADE);
    }
}
