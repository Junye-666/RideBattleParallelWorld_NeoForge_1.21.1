package com.jpigeon.ridebattleparallelworlds.common.rider.shocker;


import com.jpigeon.ridebattlelib.common.api.builder.FormBuilder;
import com.jpigeon.ridebattlelib.common.api.builder.RiderBuilder;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class ShockerConfig {
    public static final ResourceLocation SHOCKER_ID = id("shocker");
    public static final ResourceLocation SHOCKER_SLOT = id("shocker_slot");
    public static final ResourceLocation COMBATMAN_ID = id("combatman");

    public static FormConfig SHOCKER_COMBATMAN = FormBuilder.create(COMBATMAN_ID)
            .armor(
                    ModItems.Misc.SHOCKER_HELMET.get(),
                    ModItems.Misc.SHOCKER_CHESTPLATE.get(),
                    ModItems.Misc.SHOCKER_LEGGINGS.get(),
                    ModItems.Misc.SHOCKER_BOOTS.get()
            )
            .requiredItem(SHOCKER_SLOT, Items.AIR)
            .effect(MobEffects.INVISIBILITY, 0)
            .effect(MobEffects.JUMP, 0)
            .effect(MobEffects.NIGHT_VISION, 0)
            .effect(MobEffects.MOVEMENT_SPEED, 0)
            .allowsEmptyDriver(true)
            .build();

    public static RiderConfig SHOCKER = RiderBuilder.create(SHOCKER_ID)
            .driver(ModItems.Misc.SHOCKER_HELMET.get(), EquipmentSlot.HEAD)
            .slot(SHOCKER_SLOT, List.of(Items.AIR), true, true)
            .form(SHOCKER_COMBATMAN)
            .baseForm(SHOCKER_COMBATMAN.getFormId())
            .build();

    public static void init() {
        RiderRegistry.registerRider(SHOCKER);
    }
}
