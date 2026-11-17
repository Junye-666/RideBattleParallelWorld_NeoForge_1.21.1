package com.jpigeon.ridebattleparallelworlds.common.data.attachment;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.FormDataSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class PWAttachmentsHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) refreshAndSync(sp);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) refreshAndSync(sp);
    }

    private static void refreshAndSync(ServerPlayer player) {
        PWData data = player.getData(PWAttachments.PW_DATA);
        data.refreshRegisteredForms();
        player.setData(PWAttachments.PW_DATA, data);
        PacketDistributor.sendToPlayer(
                player, new FormDataSyncPacket(player.getUUID(), data));
    }
}
