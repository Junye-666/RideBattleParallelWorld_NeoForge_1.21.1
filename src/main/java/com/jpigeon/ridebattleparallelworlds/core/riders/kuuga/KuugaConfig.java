package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga;

import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class KuugaConfig {
    // 形态
    public static final ResourceLocation ARCLE_CORE = fromString("arcle_core");

    public static final ResourceLocation GROWING_ID = fromString("growing_form");
    public static final ResourceLocation MIGHTY_ID = fromString("mighty_form");
    public static final ResourceLocation DRAGON_ID = fromString("dragon_form");
    public static final ResourceLocation PEGASUS_ID = fromString("pegasus_form");
    public static final ResourceLocation TITAN_ID = fromString("titan_form");
    public static final ResourceLocation RISING_MIGHTY_ID = fromString("rising_mighty_form");
    public static final ResourceLocation RISING_DRAGON_ID = fromString("rising_dragon_form");
    public static final ResourceLocation RISING_PEGASUS_ID = fromString("rising_pegasus_form");
    public static final ResourceLocation RISING_TITAN_ID = fromString("rising_titan_form");
    public static final ResourceLocation AMAZING_MIGHTY_ID = fromString("amazing_mighty_form");
    public static final ResourceLocation ULTIMATE_ID = fromString("ultimate_form");

    public static final RiderConfig KUUGA = new RiderConfig(RiderIds.KUUGA_ID)
            .setMainDriverItem(ModItems.ARCLE.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ARCLE_CORE,
                    List.of(ModItems.MIGHTY_ELEMENT.get(), ModItems.DRAGON_ELEMENT.get(), ModItems.PEGASUS_ELEMENT.get(), ModItems.TITAN_ELEMENT.get(), ModItems.RISING_MIGHTY_ELEMENT.get(), ModItems.RISING_DRAGON_ELEMENT.get(),ModItems.RISING_PEGASUS_ELEMENT.get(), ModItems.RISING_TITAN_ELEMENT.get(), ModItems.AMAZING_MIGHTY_ELEMENT.get(), ModItems.ULTIMATE_ELEMENT.get()),
                    true,
                    true
            );

    public static final FormConfig KUUGA_GROWING_FORM = new FormConfig(GROWING_ID)
            .setArmor(
                    ModItems.GROWING_HELMET.get(),
                    ModItems.GROWING_CHESTPLATE.get(),
                    null,
                    ModItems.GROWING_BOOTS.get()
            )
            .setShouldPause(true)
            .addRequiredItem(ARCLE_CORE, Items.AIR)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addSkill(RiderSkills.GROWING_KICK)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            ;

    public static final FormConfig KUUGA_MIGHTY_FORM = new FormConfig(MIGHTY_ID)
            .setArmor(
                    ModItems.MIGHTY_HELMET.get(),
                    ModItems.MIGHTY_CHESTPLATE.get(),
                    null,
                    ModItems.MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ARCLE_CORE, ModItems.MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.MIGHTY_KICK)
            ;

    public static final FormConfig KUUGA_DRAGON_FORM = new FormConfig(DRAGON_ID)
            .setArmor(
                    ModItems.DRAGON_HELMET.get(),
                    ModItems.DRAGON_CHESTPLATE.get(),
                    null,
                    ModItems.DRAGON_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1, AttributeModifier.Operation.ADD_VALUE)
            .addRequiredItem(ARCLE_CORE, ModItems.DRAGON_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_PEGASUS_FORM = new FormConfig(PEGASUS_ID)
            .setArmor(
                    ModItems.PEGASUS_HELMET.get(),
                    ModItems.PEGASUS_CHESTPLATE.get(),
                    null,
                    ModItems.PEGASUS_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 1, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, ModItems.PEGASUS_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_TITAN_FORM = new FormConfig(TITAN_ID)
            .setArmor(
                    ModItems.TITAN_HELMET.get(),
                    ModItems.TITAN_CHESTPLATE.get(),
                    null,
                    ModItems.TITAN_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, ModItems.TITAN_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_RISING_MIGHTY_FORM = new FormConfig(RISING_MIGHTY_ID)
            .setArmor(
                    ModItems.RISING_MIGHTY_HELMET.get(),
                    ModItems.RISING_MIGHTY_CHESTPLATE.get(),
                    null,
                    ModItems.RISING_MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addRequiredItem(ARCLE_CORE, ModItems.RISING_MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.RISING_MIGHTY_KICK);

    public static final FormConfig KUUGA_RISING_DRAGON_FORM = new FormConfig(RISING_DRAGON_ID)
            .setArmor(
                    ModItems.RISING_DRAGON_HELMET.get(),
                    ModItems.RISING_DRAGON_CHESTPLATE.get(),
                    null,
                    ModItems.RISING_DRAGON_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 3, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .addAttribute(ResourceLocation.fromNamespaceAndPath("minecraft", "generic.attack_damage"), 1.7, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addRequiredItem(ARCLE_CORE, ModItems.RISING_DRAGON_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_RISING_PEGASUS_FORM = new FormConfig(RISING_PEGASUS_ID)
            .setArmor(
                    ModItems.RISING_PEGASUS_HELMET.get(),
                    ModItems.RISING_PEGASUS_CHESTPLATE.get(),
                    null,
                    ModItems.RISING_PEGASUS_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 1, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ARCLE_CORE, ModItems.RISING_PEGASUS_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_RISING_TITAN_FORM = new FormConfig(RISING_TITAN_ID)
            .setArmor(
                    ModItems.RISING_TITAN_HELMET.get(),
                    ModItems.RISING_TITAN_CHESTPLATE.get(),
                    null,
                    ModItems.RISING_TITAN_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 3, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SLOWDOWN, -1, 0, true)
            .addRequiredItem(ARCLE_CORE, ModItems.RISING_TITAN_ELEMENT.get())
            ;

    public static final FormConfig KUUGA_AMAZING_MIGHTY_FORM = new FormConfig(AMAZING_MIGHTY_ID)
            .setArmor(
                    ModItems.AMAZING_MIGHTY_HELMET.get(),
                    ModItems.AMAZING_MIGHTY_CHESTPLATE.get(),
                    null,
                    ModItems.AMAZING_MIGHTY_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)

            .addEffect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .addEffect(MobEffects.JUMP, -1, 3, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 2, true)

            .addRequiredItem(ARCLE_CORE, ModItems.AMAZING_MIGHTY_ELEMENT.get())
            .addSkill(RiderSkills.AMAZING_MIGHTY_KICK);

    public static final FormConfig KUUGA_ULTIMATE_FORM = new FormConfig(ULTIMATE_ID)
            .setArmor(
                    ModItems.ULTIMATE_HELMET.get(),
                    ModItems.ULTIMATE_CHESTPLATE.get(),
                    null,
                    ModItems.ULTIMATE_BOOTS.get()
            )
            .setShouldPause(true)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)

            .addEffect(MobEffects.DAMAGE_BOOST, -1, 5, true)
            .addEffect(MobEffects.JUMP, -1, 4, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 3, true)
            .addEffect(MobEffects.DAMAGE_RESISTANCE, -1, 3, true)

            .addRequiredItem(ARCLE_CORE, ModItems.ULTIMATE_ELEMENT.get())
            .addSkill(RiderSkills.ULTRA_KICK);


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
