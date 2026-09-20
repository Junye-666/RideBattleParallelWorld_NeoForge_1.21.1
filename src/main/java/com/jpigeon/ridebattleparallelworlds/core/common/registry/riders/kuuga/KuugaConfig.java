package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems.Kuuga.*;
import static com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds.id;

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

    public static final List<Item> KUUGA_ITEMS = List.of(MIGHTY_ELEMENT.get(), DRAGON_ELEMENT.get(), PEGASUS_ELEMENT.get(), TITAN_ELEMENT.get(), RISING_MIGHTY_ELEMENT.get(), RISING_DRAGON_ELEMENT.get(), RISING_PEGASUS_ELEMENT.get(), RISING_TITAN_ELEMENT.get(), AMAZING_MIGHTY_ELEMENT.get(), ULTIMATE_ELEMENT.get());

    public static final RiderConfig KUUGA = new RiderConfig(RiderIds.KUUGA_ID)
            .setMainDriverItem(ARCLE.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ARCLE_CORE,
                    KUUGA_ITEMS,
                    true,
                    true
            );

    public static final FormConfig KUUGA_GROWING_FORM = new FormConfig(GROWING_ID)
            .setArmor(
                    GROWING_HELMET.get(),
                    GROWING_CHESTPLATE.get(),
                    null,
                    GROWING_BOOTS.get()
            )
            .setShouldPause(true)
            .addRequiredItem(ARCLE_CORE, Items.AIR)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addSkill(RiderSkills.GROWING_KICK)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_MIGHTY_FORM = new FormConfig(MIGHTY_ID)
            .setArmor(
                    MIGHTY_HELMET.get(),
                    MIGHTY_CHESTPLATE.get(),
                    null,
                    MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 1, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ARCLE_CORE, MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.MIGHTY_KICK)
            .addSkill(RiderSkills.MIGHTY_PUNCH)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_DRAGON_FORM = new FormConfig(DRAGON_ID)
            .setArmor(
                    DRAGON_HELMET.get(),
                    DRAGON_CHESTPLATE.get(),
                    null,
                    DRAGON_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.JUMP, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1, AttributeModifier.Operation.ADD_VALUE)
            .addRequiredItem(ARCLE_CORE, DRAGON_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_PEGASUS_FORM = new FormConfig(PEGASUS_ID)
            .setArmor(
                    PEGASUS_HELMET.get(),
                    PEGASUS_CHESTPLATE.get(),
                    null,
                    PEGASUS_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 1, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, PEGASUS_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 1.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_TITAN_FORM = new FormConfig(TITAN_ID)
            .setArmor(
                    TITAN_HELMET.get(),
                    TITAN_CHESTPLATE.get(),
                    null,
                    TITAN_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, TITAN_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_RISING_MIGHTY_FORM = new FormConfig(RISING_MIGHTY_ID)
            .setArmor(
                    RISING_MIGHTY_HELMET.get(),
                    RISING_MIGHTY_CHESTPLATE.get(),
                    null,
                    RISING_MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addRequiredItem(ARCLE_CORE, RISING_MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.RISING_MIGHTY_KICK)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_RISING_DRAGON_FORM = new FormConfig(RISING_DRAGON_ID)
            .setArmor(
                    RISING_DRAGON_HELMET.get(),
                    RISING_DRAGON_CHESTPLATE.get(),
                    null,
                    RISING_DRAGON_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.JUMP, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1.7, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addRequiredItem(ARCLE_CORE, RISING_DRAGON_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_RISING_PEGASUS_FORM = new FormConfig(RISING_PEGASUS_ID)
            .setArmor(
                    RISING_PEGASUS_HELMET.get(),
                    RISING_PEGASUS_CHESTPLATE.get(),
                    null,
                    RISING_PEGASUS_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 1, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ARCLE_CORE, RISING_PEGASUS_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 1.8, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_RISING_TITAN_FORM = new FormConfig(RISING_TITAN_ID)
            .setArmor(
                    RISING_TITAN_HELMET.get(),
                    RISING_TITAN_CHESTPLATE.get(),
                    null,
                    RISING_TITAN_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, RISING_TITAN_ELEMENT.get())
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static final FormConfig KUUGA_AMAZING_MIGHTY_FORM = new FormConfig(AMAZING_MIGHTY_ID)
            .setArmor(
                    AMAZING_MIGHTY_HELMET.get(),
                    AMAZING_MIGHTY_CHESTPLATE.get(),
                    null,
                    AMAZING_MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)

            .addEffect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .addEffect(MobEffects.JUMP, -1, 3, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addRequiredItem(ARCLE_CORE, AMAZING_MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.AMAZING_MIGHTY_KICK);

    public static final FormConfig KUUGA_ULTIMATE_FORM = new FormConfig(ULTIMATE_ID)
            .setArmor(
                    ULTIMATE_HELMET.get(),
                    ULTIMATE_CHESTPLATE.get(),
                    null,
                    ULTIMATE_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)

            .addEffect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .addEffect(MobEffects.JUMP, -1, 4, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 3, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.max_health"), 5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)

            .addRequiredItem(ARCLE_CORE, ULTIMATE_ELEMENT.get())
            .addSkill(RiderSkills.ULTIMATE_KICK);


    public static void registerKuuga() {
        // 形态赋予
        KUUGA.addForm(KUUGA_GROWING_FORM);
        KUUGA_GROWING_FORM.setAllowsEmptyDriver(true);
        KUUGA.setBaseForm(KUUGA_GROWING_FORM.getFormId());

        KUUGA.addForm(KUUGA_MIGHTY_FORM);
        KUUGA.addForm(KUUGA_DRAGON_FORM);
        KUUGA.addForm(KUUGA_PEGASUS_FORM);
        KUUGA.addForm(KUUGA_TITAN_FORM);

        KUUGA.addForm(KUUGA_RISING_MIGHTY_FORM);
        KUUGA.addForm(KUUGA_RISING_DRAGON_FORM);
        KUUGA.addForm(KUUGA_RISING_PEGASUS_FORM);
        KUUGA.addForm(KUUGA_RISING_TITAN_FORM);

        KUUGA.addForm(KUUGA_AMAZING_MIGHTY_FORM);
        KUUGA.addForm(KUUGA_ULTIMATE_FORM);

        RiderRegistry.registerRider(KUUGA);
    }

    public static void init() {
        registerKuuga();
    }
}
