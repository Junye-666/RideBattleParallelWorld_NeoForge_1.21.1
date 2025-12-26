package com.jpigeon.ridebattleparallelworlds.core.riders.decade;

import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderRegistry;
import com.jpigeon.ridebattlelib.core.system.henshin.helper.TriggerType;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class DecadeConfig {
    public static final ResourceLocation DECA_CARD = fromString("decade_card_slot");
    public static final ResourceLocation DECADE_BASE_ID = fromString("decade_base");

    public static final RiderConfig DECADE = new RiderConfig(RiderIds.DECADE_ID)
            .setMainDriverItem(ModItems.DECA_DRIVER.get())
            .addMainDriverSlot(
                    DECA_CARD,
                    List.of(
                            ModItems.KAMEN_RIDE_DECADE.get(),
                            ModItems.KAMEN_RIDE_KUUGA.get(),
                            ModItems.FORM_RIDE_KUUGA_DRAGON.get(),
                            ModItems.FORM_RIDE_KUUGA_PEGASUS.get(),
                            ModItems.FORM_RIDE_KUUGA_TITAN.get()
                    ),
                    true,
                    true
                    );

    public static final FormConfig DECADE_BASE = new FormConfig(DECADE_BASE_ID)
            .setArmor(ModItems.DECADE_HELMET.get(),
                    ModItems.DECADE_CHESTPLATE.get(),
                    null,
                    ModItems.DECADE_BOOTS.get())
            .addEffect(MobEffects.INVISIBILITY, -1, 0, true)
            .addEffect(MobEffects.JUMP, -1, 0, true)
            .addEffect(MobEffects.DAMAGE_BOOST, -1, 0, true)
            .addEffect(MobEffects.MOVEMENT_SPEED, -1, 0, true)
            .addRequiredItem(DECA_CARD, ModItems.KAMEN_RIDE_DECADE.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            ;

    public static final FormConfig DECADE_KUUGA_MIGHTY = KuugaConfig.KUUGA_MIGHTY_FORM.copyWithoutItemsAndSkills()
            .addRequiredItem(DECA_CARD, ModItems.KAMEN_RIDE_KUUGA.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            ;

    public static final FormConfig DECADE_KUUGA_DRAGON = KuugaConfig.KUUGA_DRAGON_FORM.copyWithoutItemsAndSkills()
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_DRAGON.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.DRAGON_ROD.get())
            ;

    public static final FormConfig DECADE_KUUGA_PEGASUS = KuugaConfig.KUUGA_PEGASUS_FORM.copyWithoutItemsAndSkills()
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_PEGASUS.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.PEGASUS_BOWGUN.get())
            ;

    public static final FormConfig DECADE_KUUGA_TITAN = KuugaConfig.KUUGA_TITAN_FORM.copyWithoutItemsAndSkills()
            .addRequiredItem(DECA_CARD, ModItems.FORM_RIDE_KUUGA_TITAN.get())
            .setShouldPause(true)
            .setTriggerType(TriggerType.AUTO)
            .addGrantedItem(ModItems.TITAN_SWORD.get())
            ;


    private static void registerDecade() {
        DECADE.addForm(DECADE_BASE);

        DECADE.addForm(DECADE_KUUGA_MIGHTY);
        DECADE.addForm(DECADE_KUUGA_DRAGON);
        DECADE.addForm(DECADE_KUUGA_PEGASUS);
        DECADE.addForm(DECADE_KUUGA_TITAN);

        RiderRegistry.registerRider(DECADE);
    }


    public static void init(){
        registerDecade();
    }
}
