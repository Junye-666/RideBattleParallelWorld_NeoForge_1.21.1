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

import java.util.List;

public class KuugaConfig {
    public static final ResourceLocation ARCLE_CORE = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "arcle_core");
    public static final ResourceLocation MIGHTY_ID = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mighty_form");

    public static final RiderConfig KUUGA = new RiderConfig(RiderIds.KUUGA_ID)
            .setMainDriverItem(ModItems.ARCLE.get(), EquipmentSlot.LEGS)
            .addMainDriverSlot(ARCLE_CORE,
                    List.of(ModItems.MIGHTY_ELEMENT.get()),
                    true,
                    false
                    )
            ;

    public static final FormConfig KUUGA_MIGHTY_FORM = new FormConfig(MIGHTY_ID)
            .setArmor(
                    ModItems.KUUGA_HELMET.get(),
                    ModItems.KUUGA_CHESTPLATE.get(),
                    null,
                    ModItems.KUUGA_BOOTS.get()
            )
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 2, true)
            .addRequiredItem(ARCLE_CORE, ModItems.MIGHTY_ELEMENT.get())
            ;

    public static void registerKuuga(){
        KUUGA.addForm(KUUGA_MIGHTY_FORM);

        KUUGA_MIGHTY_FORM.setShouldPause(true);

        RiderRegistry.registerRider(KUUGA);
    }

    public static void init(){
        registerKuuga();
    }
}
