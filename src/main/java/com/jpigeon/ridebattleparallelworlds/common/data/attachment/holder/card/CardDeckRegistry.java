package com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CardDeckRegistry {
    private CardDeckRegistry() {}

    private static final Map<ResourceLocation, List<ResourceLocation>> CARDS = new HashMap<>();

    /** 由骑士 pack 在 registerCommon 里调用一次。 */
    public static void registerCards(ResourceLocation riderId, List<ResourceLocation> cardIds) {
        CARDS.put(riderId, List.copyOf(cardIds));
    }

    public static List<ResourceLocation> getCards(ResourceLocation riderId) {
        return CARDS.getOrDefault(riderId, List.of());
    }

    public static boolean isCard(ResourceLocation riderId, ResourceLocation cardId) {
        return CARDS.getOrDefault(riderId, List.of()).contains(cardId);
    }
}
