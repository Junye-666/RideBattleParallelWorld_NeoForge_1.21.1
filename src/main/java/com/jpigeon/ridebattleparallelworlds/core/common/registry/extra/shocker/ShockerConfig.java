package com.jpigeon.ridebattleparallelworlds.core.common.registry.extra.shocker;


import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds.fromString;

public class ShockerConfig {
    public static final ResourceLocation SHOCKER_ID = fromString("shocker");
    public static final ResourceLocation SHOCKER_SLOT = fromString("shocker_slot");
    public static final ResourceLocation COMBATMAN_ID = fromString("combatman");

    public static RiderConfig SHOCKER = new RiderConfig(SHOCKER_ID)
            .setMainDriverItem(ModItems.SHOCKER_HELMET.get(), EquipmentSlot.HEAD)
            .addMainDriverSlot(SHOCKER_SLOT, List.of(Items.AIR), true, true);

    public static FormConfig SHOCKER_COMBATMAN = new FormConfig(COMBATMAN_ID)
            .setArmor(
                    ModItems.SHOCKER_HELMET.get(),
                    ModItems.SHOCKER_CHESTPLATE.get(),
                    ModItems.SHOCKER_LEGGINGS.get(),
                    ModItems.SHOCKER_BOOTS.get()
            )
            .addRequiredItem(SHOCKER_SLOT, Items.AIR)
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.NIGHT_VISION, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 0, true);

    private static void registerShocker() {
        SHOCKER.addForm(SHOCKER_COMBATMAN);
        SHOCKER.setBaseForm(SHOCKER_COMBATMAN.getFormId());
        SHOCKER_COMBATMAN.setAllowsEmptyDriver(true);

        RiderRegistry.registerRider(SHOCKER);
    }

    public static void init() {
        registerShocker();
    }
}
