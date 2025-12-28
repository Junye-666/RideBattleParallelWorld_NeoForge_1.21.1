package com.jpigeon.ridebattleparallelworlds.core.network;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.core.network.packet.PWDataSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Map;

public class PWPacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleParallelWorlds.MODID)
                .versioned("0.0.1")
                .playToClient(
                        PWDataSyncPacket.TYPE,
                        PWDataSyncPacket.STREAM_CODEC,
                        (payload, context) -> {
                            Player clientPlayer = context.player();

                            // 只应用给对应玩家
                            if (!clientPlayer.getUUID().equals(payload.playerId())) return;

                            PWData data = clientPlayer.getData(PWAttachments.PW_DATA);

                            // ✅ 修复：同步完整的 FormUnlockData
                            Map<ResourceLocation, Map<ResourceLocation, Boolean>> allUnlockData =
                                    payload.data().getFormUnlockData().getAllUnlockData();

                            // 清空现有数据并重新设置
                            data.clearAllFormUnlockData();

                            for (Map.Entry<ResourceLocation, Map<ResourceLocation, Boolean>> riderEntry : allUnlockData.entrySet()) {
                                ResourceLocation riderId = riderEntry.getKey();
                                for (Map.Entry<ResourceLocation, Boolean> formEntry : riderEntry.getValue().entrySet()) {
                                    if (formEntry.getValue()) {
                                        data.unlockForm(riderId, formEntry.getKey());
                                    } else {
                                        data.lockForm(riderId, formEntry.getKey());
                                    }
                                }
                            }
                        }
                );
    }

    public static void sendToServer(CustomPacketPayload packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(packet);
        }
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        player.connection.send(packet);
    }
}