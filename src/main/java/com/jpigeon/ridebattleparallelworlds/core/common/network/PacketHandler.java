package com.jpigeon.ridebattleparallelworlds.core.common.network;

import com.jpigeon.ridebattlelib.client.cache.ClientTransformedCache;
import com.jpigeon.ridebattlelib.client.network.ClientRiderSyncManager;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWClientSkillPacket;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWDataSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class PacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleParallelWorlds.MODID)
                .versioned("0.0.3").optional()
                .playToClient(PWDataSyncPacket.TYPE, PWDataSyncPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() -> {
                            Player p = ctx.player();
                            if (!p.getUUID().equals(payload.playerId())) return;
                            applyUnlockData(p, payload.data());
                        })
                )
                .playToClient(
                        PWClientSkillPacket.TYPE, PWClientSkillPacket.STREAM_CODEC,
                        (payload, ctx) -> ctx.enqueueWork(() -> {
                            LocalPlayer p = Minecraft.getInstance().player;
                            if (p == null) return;
                            ResourceLocation riderId = ClientTransformedCache.getRiderId(p.getUUID());
                            ClientRiderSyncManager.applySkill(riderId, payload.skillId());
                        })
                );
    }

    private static void applyUnlockData(Player p, PWData data) {
        PWData local = p.getData(PWAttachments.PW_DATA);
        local.replaceAllUnlockData(data.getFormUnlockData().getAllUnlockData());
    }
}