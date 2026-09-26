package com.jpigeon.ridebattleparallelworlds.common.rider.agito;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

    public static final List<Item> AGITO_ITEMS = List.of(GROUND_ELEMENT.get(), FLAME_ELEMENT.get(), STORM_ELEMENT.get(), TRINITY_ELEMENT.get(), BURNING_ELEMENT.get(), SHINING_ELEMENT.get());

    public static final RiderConfig AGITO = new RiderConfig(RiderIds.AGITO_ID)
            .setMainDriverItem(ALTER_RING.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ALTER_RING_CORE,
                    AGITO_ITEMS,
                    true,
                    true
            )
            .addBaseAttribute(ResourceLocation.withDefaultNamespace("generic.jump_strength"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addBaseAttribute(ResourceLocation.withDefaultNamespace("generic.water_movement_efficiency"), 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public static final FormConfig AGITO_GROUND_FORM = new FormConfig(GROUND_ID)
            .setArmor(
                    GROUND_HELMET.get(),
                    GROUND_CHESTPLATE.get(),
                    null,
                    GROUND_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, GROUND_ELEMENT.get())
            .addAttribute(ResourceLocation.withDefaultNamespace("generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.GROUND_KICK);

    public static final FormConfig AGITO_FLAME_FORM = new FormConfig(FLAME_ID)
            .setArmor(
                    FLAME_HELMET.get(),
                    FLAME_CHESTPLATE.get(),
                    null,
                    FLAME_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, FLAME_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.FLAME_SABER);

    public static final FormConfig AGITO_STORM_FORM = new FormConfig(STORM_ID)
            .setArmor(
                    STORM_HELMET.get(),
                    STORM_CHESTPLATE.get(),
                    null,
                    STORM_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 1, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, STORM_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.STORM_HALBERD);

    public static final FormConfig AGITO_TRINITY_FORM = new FormConfig(TRINITY_ID)
            .setArmor(
                    TRINITY_HELMET.get(),
                    TRINITY_CHESTPLATE.get(),
                    null,
                    TRINITY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, TRINITY_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.TRINITY_WEAPON);

    public static final FormConfig AGITO_BURNING_FORM = new FormConfig(BURNING_ID)
            .setArmor(
                    BURNING_HELMET.get(),
                    BURNING_CHESTPLATE.get(),
                    null,
                    BURNING_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addEffect(MobEffects.FIRE_RESISTANCE, -1, 0, true)
            .addRequiredItem(ALTER_RING_CORE, BURNING_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.SHINING_CALIBUR);


    private static void registerAgito() {
        AGITO.addForm(AGITO_GROUND_FORM);
        AGITO.addForm(AGITO_FLAME_FORM);
        AGITO.addForm(AGITO_STORM_FORM);
        AGITO.addForm(AGITO_TRINITY_FORM);
        AGITO.addForm(AGITO_BURNING_FORM);

        RiderRegistry.registerRider(AGITO);
    }

    public static void init() {
        registerAgito();
    }
}
