package com.jpigeon.ridebattleparallelworlds.common.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class CardDrawnEvent extends Event {
    private final Player player;
    private final ResourceLocation riderId;
    private final ResourceLocation cardId;

    public CardDrawnEvent(Player player, ResourceLocation riderId, ResourceLocation cardId) {
        this.player = player;
        this.riderId = riderId;
        this.cardId = cardId;
    }

    public Player getPlayer() { return player; }
    public ResourceLocation getRiderId() { return riderId; }
    public ResourceLocation getCardId() { return cardId; }
}
