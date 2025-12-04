package com.jpigeon.ridebattleparallelworlds.core.riders.kuuga;

import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import java.util.List;

public class KuugaConfig {
    public static final ResourceLocation ARCLE_CORE = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "arcle_core");
    public static final ResourceLocation MIGHTY_ID = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mighty_form");
    public static final ResourceLocation DRAGON_ID = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "dragon_form");

    public static final RiderConfig KUUGA = new RiderConfig(RiderIds.KUUGA_ID)
            .setMainDriverItem(ModItems.ARCLE.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ARCLE_CORE,
                    List.of(ModItems.MIGHTY_ELEMENT.get(), ModItems.DRAGON_ELEMENT.get()),
                    true,
                    false
            );

    public static final FormConfig KUUGA_MIGHTY_FORM = new FormConfig(MIGHTY_ID)
            .setArmor(
                    ModItems.MIGHTY_HELMET.get(),
                    ModItems.MIGHTY_CHESTPLATE.get(),
                    null,
                    ModItems.MIGHTY_BOOTS.get()
            )
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .addRequiredItem(ARCLE_CORE, ModItems.MIGHTY_ELEMENT.get());

    public static final FormConfig KUUGA_DRAGON_FORM = new FormConfig(DRAGON_ID)
            .setArmor(
                    ModItems.DRAGON_HELMET.get(),
                    ModItems.DRAGON_CHESTPLATE.get(),
                    null,
                    ModItems.DRAGON_BOOTS.get()
            )
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 2, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 2, true)
            .addRequiredItem(ARCLE_CORE, ModItems.DRAGON_ELEMENT.get())
            ;

    public static void registerKuuga() {
        KUUGA.addForm(KUUGA_MIGHTY_FORM);
        KUUGA.addForm(KUUGA_DRAGON_FORM);

        KUUGA_MIGHTY_FORM.setShouldPause(true);
        KUUGA_DRAGON_FORM.setShouldPause(true);

        RiderRegistry.registerRider(KUUGA);
    }

    public static void init() {
        registerKuuga();
    }
}
