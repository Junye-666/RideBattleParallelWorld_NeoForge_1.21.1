package com.jpigeon.ridebattleparallelworlds.common.rider.faiz;

import com.jpigeon.ridebattlelib.common.api.builder.FormBuilder;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Faiz.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class FaizConfig {
    public static final ResourceLocation FAIZ_DRIVER = id("faiz_driver_slot");
    public static final ResourceLocation FAIZ_BASE_ID = id("faiz_base");

    public static final FormConfig FAIZ_BASE_FORM = FormBuilder.create(FAIZ_BASE_ID)
            .armor(
                    FAIZ_BASE_HELMET.get(),
                    FAIZ_BASE_CHESTPLATE.get(),
                    null,
                    FAIZ_BASE_BOOTS.get()
            )
            .shouldPause(true)
            .effect(MobEffects.DAMAGE_BOOST, -1, 1, true)
            .effect(MobEffects.NIGHT_VISION, -1, 0, true)
            .effect(MobEffects.MOVEMENT_SPEED, -1, 1, true)
            .requiredItem(FAIZ_DRIVER, Items.BRICK) // TODO

            .attribute(Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .build();
}
