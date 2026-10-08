package com.jpigeon.ridebattleparallelworlds.common.rider.agito;

import com.jpigeon.ridebattlelib.common.api.builder.FormBuilder;
import com.jpigeon.ridebattlelib.common.api.builder.RiderBuilder;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Agito.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class AgitoConfig {
    public static final ResourceLocation ALTER_RING_CORE = id("alter_ring_core");

    public static final ResourceLocation GROUND_ID = id("ground_form");
    public static final ResourceLocation FLAME_ID = id("flame_form");
    public static final ResourceLocation STORM_ID = id("storm_form");
    public static final ResourceLocation TRINITY_ID = id("trinity_form");
    public static final ResourceLocation BURNING_ID = id("burning_form");
    public static final ResourceLocation SHINING_ID = id("shining_form");

    public static List<Item> agitoItems() {
        return List.of(GROUND_ELEMENT.get(), FLAME_ELEMENT.get(), STORM_ELEMENT.get(), TRINITY_ELEMENT.get(), BURNING_ELEMENT.get(), SHINING_ELEMENT.get());
    }

    public static final FormConfig AGITO_GROUND_FORM = FormBuilder.create(GROUND_ID)
            .armor(
                    GROUND_HELMET.get(),
                    GROUND_CHESTPLATE.get(),
                    null,
                    GROUND_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, 2)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 1)
            .requiredItem(ALTER_RING_CORE, GROUND_ELEMENT.get())
            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.GROUND_KICK)
            .build();


    public static final FormConfig AGITO_FLAME_FORM = FormBuilder.create(FLAME_ID)
            .armor(
                    FLAME_HELMET.get(),
                    FLAME_CHESTPLATE.get(),
                    null,
                    FLAME_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, 2)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 1)
            .requiredItem(ALTER_RING_CORE, FLAME_ELEMENT.get())
            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.FLAME_SABER)
            .build();

    public static final FormConfig AGITO_STORM_FORM = FormBuilder.create(STORM_ID)
            .armor(
                    STORM_HELMET.get(),
                    STORM_CHESTPLATE.get(),
                    null,
                    STORM_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, 1)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 1)
            .requiredItem(ALTER_RING_CORE, STORM_ELEMENT.get())
            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.STORM_HALBERD)
            .build();

    public static final FormConfig AGITO_TRINITY_FORM = FormBuilder.create(TRINITY_ID)
            .armor(
                    TRINITY_HELMET.get(),
                    TRINITY_CHESTPLATE.get(),
                    null,
                    TRINITY_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, 2)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 1)
            .requiredItem(ALTER_RING_CORE, TRINITY_ELEMENT.get())
            .attribute(Attributes.MAX_HEALTH, 3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.TRINITY_WEAPON)
            .build();

    public static final FormConfig AGITO_BURNING_FORM = FormBuilder.create(BURNING_ID)
            .armor(
                    BURNING_HELMET.get(),
                    BURNING_CHESTPLATE.get(),
                    null,
                    BURNING_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, 3)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 1)
            .effect(MobEffects.FIRE_RESISTANCE, 0)
            .requiredItem(ALTER_RING_CORE, BURNING_ELEMENT.get())
            .attribute(Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.SHINING_CALIBUR)
            .build();

    public static final RiderConfig AGITO = RiderBuilder.create(RiderIds.AGITO_ID)
            .driver(ALTER_RING.get(), EquipmentSlot.LEGS)
            .slot(
                    ALTER_RING_CORE,
                    agitoItems(),
                    true,
                    true
            )
            .baseAttribute(Attributes.JUMP_STRENGTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .baseAttribute(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .form(AGITO_GROUND_FORM)
            .form(AGITO_FLAME_FORM)
            .form(AGITO_STORM_FORM)
            .form(AGITO_TRINITY_FORM)
            .form(AGITO_BURNING_FORM)
            .baseForm(AGITO_GROUND_FORM.getFormId())
            .build();

    public static void init() {
        RiderRegistry.registerRider(AGITO);
    }
}
