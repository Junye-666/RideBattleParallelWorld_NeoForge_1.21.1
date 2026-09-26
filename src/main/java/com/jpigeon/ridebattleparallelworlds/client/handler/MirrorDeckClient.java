package com.jpigeon.ridebattleparallelworlds.client.handler;

import com.jpigeon.ridebattleparallelworlds.client.screen.MirrorCardScreen;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.DeckPackets;
import net.minecraft.client.Minecraft;

public final class MirrorDeckClient {
    private MirrorDeckClient() {}

    public static void handleSync(DeckPackets.DeckSyncPacket p) {
        Minecraft.getInstance().setScreen(
                new MirrorCardScreen(p.riderId(), p.order(), p.drawn()));
    }
}
