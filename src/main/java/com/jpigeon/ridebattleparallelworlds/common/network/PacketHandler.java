package com.jpigeon.ridebattleparallelworlds.common.network;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.client.handler.MirrorDeckClient;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.DeckPackets;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.FormDataSyncPacket;
import com.jpigeon.ridebattleparallelworlds.server.handler.MirrorDeckHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleParallelWorlds.MODID)
                .versioned("0.0.3").optional()
                // S - C
                .playToClient(FormDataSyncPacket.TYPE, FormDataSyncPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() -> {
                            Player p = ctx.player();
                            if (!p.getUUID().equals(payload.playerId())) return;
                            applyUnlockData(p, payload.data());
                        })
                )

                // C - S
                .playToServer(DeckPackets.OpenDeckPacket.TYPE, DeckPackets.OpenDeckPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() ->
                                MirrorDeckHandler.onOpen(ctx.player(), payload.riderId())))
                .playToServer(DeckPackets.SaveDeckOrderPacket.TYPE, DeckPackets.SaveDeckOrderPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() ->
                                MirrorDeckHandler.onSave(ctx.player(), payload.riderId(), payload.order())))
                .playToServer(DeckPackets.DrawCardPacket.TYPE, DeckPackets.DrawCardPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() ->
                                MirrorDeckHandler.onDraw(ctx.player(), payload.riderId())))
                .playToClient(DeckPackets.DeckSyncPacket.TYPE, DeckPackets.DeckSyncPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() ->
                                MirrorDeckClient.handleSync(payload)))
        ;
    }

    private static void applyUnlockData(Player p, PWData data) {
        PWData local = p.getData(PWAttachments.PW_DATA);
        local.getFormUnlockData().replaceAll(data.getFormUnlockData().getAllUnlockData());
        local.getCardData().copyFrom(data.getCardData());
    }
}
