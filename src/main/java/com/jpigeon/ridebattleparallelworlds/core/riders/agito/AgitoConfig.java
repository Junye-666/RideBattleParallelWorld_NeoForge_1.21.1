package com.jpigeon.ridebattleparallelworlds.core.riders.agito;

import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class AgitoConfig {
    public static final ResourceLocation ALTER_RING_CORE = fromString("alter_ring_core");

    public static final ResourceLocation GROUND_ID = fromString("ground_form");
    public static final ResourceLocation FLAME_ID = fromString("flame_form");
    public static final ResourceLocation STORM_ID = fromString("storm_form");
    public static final ResourceLocation TRINITY_ID = fromString("trinity_form");
    public static final ResourceLocation BURNING_ID = fromString("burning_form");

    public static final RiderConfig AGITO = new RiderConfig(RiderIds.AGITO_ID)
            .setMainDriverItem(ModItems.ALTER_RING.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ALTER_RING_CORE,
                    List.of(ModItems.GROUND_ELEMENT.get(), ModItems.FLAME_ELEMENT.get(), ModItems.STORM_ELEMENT.get(), ModItems.TRINITY_ELEMENT.get(), ModItems.BURNING_ELEMENT.get()),
                    true,
                    true
            );

    public static final FormConfig AGITO_GROUND_FORM = new FormConfig(GROUND_ID)
            .setArmor(
                    ModItems.GROUND_HELMET.get(),
                    ModItems.GROUND_CHESTPLATE.get(),
                    null,
                    ModItems.GROUND_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, ModItems.GROUND_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.GROUND_KICK)
            ;

    public static final FormConfig AGITO_FLAME_FORM = new FormConfig(FLAME_ID)
            .setArmor(
                    ModItems.FLAME_HELMET.get(),
                    ModItems.FLAME_CHESTPLATE.get(),
                    null,
                    ModItems.FLAME_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, ModItems.FLAME_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.FLAME_SABER)
            ;

    public static final FormConfig AGITO_STORM_FORM = new FormConfig(STORM_ID)
            .setArmor(
                    ModItems.STORM_HELMET.get(),
                    ModItems.STORM_CHESTPLATE.get(),
                    null,
                    ModItems.STORM_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 1, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, ModItems.STORM_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.STORM_HALBERD)
            ;

    public static final FormConfig AGITO_TRINITY_FORM = new FormConfig(TRINITY_ID)
            .setArmor(
                    ModItems.TRINITY_HELMET.get(),
                    ModItems.TRINITY_CHESTPLATE.get(),
                    null,
                    ModItems.TRINITY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ALTER_RING_CORE, ModItems.TRINITY_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addSkill(RiderSkills.TRINITY_WEAPON)
            ;

    public static final FormConfig AGITO_BURNING_FORM = new FormConfig(BURNING_ID)
            .setArmor(
                    ModItems.BURNING_HELMET.get(),
                    ModItems.BURNING_CHESTPLATE.get(),
                    null,
                    ModItems.BURNING_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addEffect(MobEffects.FIRE_RESISTANCE, -1, 0, true)
            .addRequiredItem(ALTER_RING_CORE, ModItems.BURNING_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            ;


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
