package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga;

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
import net.minecraft.world.item.Items;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class KuugaConfig {
    // 形态
    public static final ResourceLocation ARCLE_CORE = id("arcle_core");

    public static final ResourceLocation GROWING_ID = id("growing_form");
    public static final ResourceLocation MIGHTY_ID = id("mighty_form");
    public static final ResourceLocation DRAGON_ID = id("dragon_form");
    public static final ResourceLocation PEGASUS_ID = id("pegasus_form");
    public static final ResourceLocation TITAN_ID = id("titan_form");
    public static final ResourceLocation RISING_MIGHTY_ID = id("rising_mighty_form");
    public static final ResourceLocation RISING_DRAGON_ID = id("rising_dragon_form");
    public static final ResourceLocation RISING_PEGASUS_ID = id("rising_pegasus_form");
    public static final ResourceLocation RISING_TITAN_ID = id("rising_titan_form");
    public static final ResourceLocation AMAZING_MIGHTY_ID = id("amazing_mighty_form");
    public static final ResourceLocation ULTIMATE_ID = id("ultimate_form");

    public static List<Item> kuugaItems() {
        return List.of(MIGHTY_ELEMENT.get(), DRAGON_ELEMENT.get(), PEGASUS_ELEMENT.get(), TITAN_ELEMENT.get(), RISING_MIGHTY_ELEMENT.get(), RISING_DRAGON_ELEMENT.get(), RISING_PEGASUS_ELEMENT.get(), RISING_TITAN_ELEMENT.get(), AMAZING_MIGHTY_ELEMENT.get(), ULTIMATE_ELEMENT.get());
    }

    public static final FormConfig KUUGA_GROWING_FORM = FormBuilder.create(GROWING_ID)
            .armor(
                    GROWING_HELMET.get(),
                    GROWING_CHESTPLATE.get(),
                    null,
                    GROWING_BOOTS.get()
            )
            .shouldPause(true)
            .requiredItem(ARCLE_CORE, Items.AIR)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .skill(RiderSkills.GROWING_KICK)
            .attribute(Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .attribute(Attributes.MAX_HEALTH, 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .allowsEmptyDriver(true)
            .build();

    public static final FormConfig KUUGA_MIGHTY_FORM = FormBuilder.create(MIGHTY_ID)
            .armor(
                    MIGHTY_HELMET.get(),
                    MIGHTY_CHESTPLATE.get(),
                    null,
                    MIGHTY_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 1, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .requiredItem(ARCLE_CORE, MIGHTY_ELEMENT.get())
            .skill(RiderSkills.MIGHTY_KICK)
            .skill(RiderSkills.MIGHTY_PUNCH)
            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .build();

    public static final FormConfig KUUGA_DRAGON_FORM = FormBuilder.create(DRAGON_ID)
            .armor(
                    DRAGON_HELMET.get(),
                    DRAGON_CHESTPLATE.get(),
                    null,
                    DRAGON_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.JUMP, -1, 2, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .attribute(Attributes.ATTACK_DAMAGE, 1, AttributeModifier.Operation.ADD_VALUE)
            .attribute(Attributes.MAX_HEALTH, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, DRAGON_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_PEGASUS_FORM = FormBuilder.create(PEGASUS_ID)
            .armor(
                    PEGASUS_HELMET.get(),
                    PEGASUS_CHESTPLATE.get(),
                    null,
                    PEGASUS_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.JUMP, -1, 0, true)
            .effect(MobEffects.NIGHT_VISION, -1, 1, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .attribute(Attributes.MAX_HEALTH, 1.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, PEGASUS_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_TITAN_FORM = FormBuilder.create(TITAN_ID)
            .armor(
                    TITAN_HELMET.get(),
                    TITAN_CHESTPLATE.get(),
                    null,
                    TITAN_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .attribute(Attributes.MAX_HEALTH, 2.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, TITAN_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_RISING_MIGHTY_FORM = FormBuilder.create(RISING_MIGHTY_ID)
            .armor(
                    RISING_MIGHTY_HELMET.get(),
                    RISING_MIGHTY_CHESTPLATE.get(),
                    null,
                    RISING_MIGHTY_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .attribute(Attributes.MAX_HEALTH, 2.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.RISING_MIGHTY_KICK)
            .requiredItem(ARCLE_CORE, RISING_MIGHTY_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_RISING_DRAGON_FORM = FormBuilder.create(RISING_DRAGON_ID)
            .armor(
                    RISING_DRAGON_HELMET.get(),
                    RISING_DRAGON_CHESTPLATE.get(),
                    null,
                    RISING_DRAGON_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.JUMP, -1, 3, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .attribute(Attributes.ATTACK_DAMAGE, 1.7, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, RISING_DRAGON_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_RISING_PEGASUS_FORM = FormBuilder.create(RISING_PEGASUS_ID)
            .armor(
                    RISING_PEGASUS_HELMET.get(),
                    RISING_PEGASUS_CHESTPLATE.get(),
                    null,
                    RISING_PEGASUS_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.JUMP, -1, 0, true)
            .effect(MobEffects.NIGHT_VISION, -1, 1, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .attribute(Attributes.MAX_HEALTH, 1.8, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, RISING_PEGASUS_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_RISING_TITAN_FORM = FormBuilder.create(RISING_TITAN_ID)
            .armor(
                    RISING_TITAN_HELMET.get(),
                    RISING_TITAN_CHESTPLATE.get(),
                    null,
                    RISING_TITAN_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .effect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .attribute(Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .requiredItem(ARCLE_CORE, RISING_TITAN_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_AMAZING_MIGHTY_FORM = FormBuilder.create(AMAZING_MIGHTY_ID)
            .armor(
                    AMAZING_MIGHTY_HELMET.get(),
                    AMAZING_MIGHTY_CHESTPLATE.get(),
                    null,
                    AMAZING_MIGHTY_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)

            .effect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .effect(MobEffects.JUMP, -1, 3, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .effect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)
            .attribute(Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.AMAZING_MIGHTY_KICK)
            .requiredItem(ARCLE_CORE, AMAZING_MIGHTY_ELEMENT.get())
            .build();

    public static final FormConfig KUUGA_ULTIMATE_FORM = FormBuilder.create(ULTIMATE_ID)
            .armor(
                    ULTIMATE_HELMET.get(),
                    ULTIMATE_CHESTPLATE.get(),
                    null,
                    ULTIMATE_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)

            .effect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .effect(MobEffects.JUMP, -1, 4, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .effect(MobEffects.DAMAGE_RESISTANCE, -1, 3, true)
            .attribute(Attributes.MAX_HEALTH, 5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .skill(RiderSkills.ULTIMATE_KICK)
            .requiredItem(ARCLE_CORE, ULTIMATE_ELEMENT.get())
            .build();

    public static final RiderConfig KUUGA = RiderBuilder.create(RiderIds.KUUGA_ID)
            .driver(ARCLE.get(), EquipmentSlot.LEGS)
            .slot(
                    ARCLE_CORE,
                    kuugaItems(),
                    true,
                    true
            )
            .form(KUUGA_GROWING_FORM)
            .form(KUUGA_MIGHTY_FORM)
            .form(KUUGA_DRAGON_FORM)
            .form(KUUGA_PEGASUS_FORM)
            .form(KUUGA_TITAN_FORM)

            .form(KUUGA_RISING_MIGHTY_FORM)
            .form(KUUGA_RISING_DRAGON_FORM)
            .form(KUUGA_RISING_PEGASUS_FORM)
            .form(KUUGA_RISING_TITAN_FORM)

            .form(KUUGA_AMAZING_MIGHTY_FORM)
            .form(KUUGA_ULTIMATE_FORM)

            .baseForm(KUUGA_GROWING_FORM.getFormId())
            .build();

    public static void init() {
        RiderRegistry.registerRider(KUUGA);
    }
}
