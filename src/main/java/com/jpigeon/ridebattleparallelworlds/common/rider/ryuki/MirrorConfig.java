package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki;

import com.jpigeon.ridebattlelib.common.api.builder.FormBuilder;
import com.jpigeon.ridebattlelib.common.api.builder.RiderBuilder;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.config.TriggerType;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card.CardDeckRegistry;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Ryuki.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class MirrorConfig {
    public static final ResourceLocation V_DECK = id("v_deck_slot");
    public static final ResourceLocation MIRROR_BLANK = id("mirror_blank");
    public static final ResourceLocation RYUKI_BASE_ID = id("ryuki_base");

    public static FormConfig RYUKI_BASE = FormBuilder.create(RYUKI_BASE_ID)
            .armor(
                    RYUKI_BASE_HELMET.get(),
                    RYUKI_BASE_CHESTPLATE.get(),
                    null,
                    RYUKI_BASE_BOOTS.get()
            )
            .triggerType(TriggerType.AUTO)
            .requiredItem(V_DECK, RYUKI_DECK.get())
            .shouldPause(true)
            .build();

    public static RiderConfig MIRROR_SYSTEM = RiderBuilder.create(RiderIds.MIRROR_SYSTEM_ID)
            .driver(V_BUCKLE.get())
            .slot(
                    V_DECK,
                    List.of(
                            BLANK_DECK.get(),
                            RYUKI_DECK.get()

                    ),
                    true, false
            )
            .form(RYUKI_BASE)

            .baseForm(RYUKI_BASE.getFormId())
            .build();

    public static void init() {
        RiderRegistry.registerRider(MIRROR_SYSTEM);

        registerCards();
    }

    private static void registerCards() {
        // 卡池 key 用 RYUKI_BASE_ID（formId），与 RiderIds.MIRROR_SYSTEM_ID 区分开
        registerCards(RYUKI_BASE_ID, List.of(
                id("ryuki_sword_vent_card"),
                id("ryuki_strike_vent_card"),
                id("ryuki_guard_vent_card"),
                id("ryuki_advent_card"),
                id("ryuki_final_vent_card")
        ));

    }

    private static void registerCards(ResourceLocation formId, List<ResourceLocation> cards) {
        CardDeckRegistry.registerCards(formId, cards);
    }
}
