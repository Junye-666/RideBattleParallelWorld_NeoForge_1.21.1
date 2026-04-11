package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class MirrorConfig {
    public static final ResourceLocation MIRROR_SYSTEM_ID = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mirror_system");
    public static final ResourceLocation V_DECK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "v_deck_slot");
    public static final ResourceLocation RYUKI_BASE_ID = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "ryuki_base");

    public static RiderConfig MIRROR_SYSTEM = new RiderConfig(MIRROR_SYSTEM_ID)
            .setMainDriverItem(ModItems.V_BUCKLE.get())
            .addMainDriverSlot(V_DECK,
                    List.of(
                            ModItems.RYUKI_DECK.get()

                    ),
                    true, false

            );

    public static FormConfig RYUKI_BASE = new FormConfig(RYUKI_BASE_ID)
            .setArmor(
                    ModItems.RYUKI_BASE_HELMET.get(),
                    ModItems.RYUKI_BASE_CHESTPLATE.get(),
                    null,
                    ModItems.RYUKI_BASE_BOOTS.get()
            )
            .addRequiredItem(V_DECK, ModItems.RYUKI_DECK.get())
            ;



    private static void registerMirrorSystem() {
        MIRROR_SYSTEM.addForm(RYUKI_BASE);

        RiderRegistry.registerRider(MIRROR_SYSTEM);
    }

    public static void init() {
        registerMirrorSystem();
    }
}
