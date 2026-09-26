package com.jpigeon.ridebattleparallelworlds.common.network;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.FormDataSyncPacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleParallelWorlds.MODID)
                .versioned("0.0.3").optional()
                .playToClient(FormDataSyncPacket.TYPE, FormDataSyncPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() -> {
                            Player p = ctx.player();
                            if (!p.getUUID().equals(payload.playerId())) return;
                            applyUnlockData(p, payload.data());
                        })
                )
        ;
    }

    private static void applyUnlockData(Player p, PWData data) {
        PWData local = p.getData(PWAttachments.PW_DATA);
        local.replaceAllUnlockData(data.getFormUnlockData().getAllUnlockData());
    }
}
