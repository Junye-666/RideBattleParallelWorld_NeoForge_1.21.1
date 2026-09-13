package com.jpigeon.ridebattleparallelworlds.core.common.network;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.RiderHandlerClient;
import com.jpigeon.ridebattleparallelworlds.core.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWClientSkillPacket;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWDataSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Map;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleParallelWorlds.MODID)
                .versioned("0.0.3").optional()
                .playToClient(
                        PWDataSyncPacket.TYPE,
                        PWDataSyncPacket.STREAM_CODEC,
                        (payload, context) -> {
                            Player clientPlayer = context.player();

                            // 只应用给对应玩家
                            if (!clientPlayer.getUUID().equals(payload.playerId())) return;

                            PWData data = clientPlayer.getData(PWAttachments.PW_DATA);

                            // 同步完整的 FormUnlockData
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
                )
                .playToClient(
                        PWClientSkillPacket.TYPE,
                        PWClientSkillPacket.STREAM_CODEC,
                        (payload, context) -> context.enqueueWork(() -> {
                            Player clientPlayer = context.player();
                            RiderHandlerClient.handleClientSkillEvent(clientPlayer, payload.skillId());
                        })
                )
        ;
    }
}