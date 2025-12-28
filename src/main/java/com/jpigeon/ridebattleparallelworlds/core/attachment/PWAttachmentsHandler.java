package com.jpigeon.ridebattleparallelworlds.core.attachment;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.network.PWPacketHandler;
import com.jpigeon.ridebattleparallelworlds.core.network.packet.PWDataSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;

public class PWAttachmentsHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PWData data = serverPlayer.getData(PWAttachments.PW_DATA);
        PWPacketHandler.sendToClient(
                serverPlayer,
                new PWDataSyncPacket(serverPlayer.getUUID(), data)
        );
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        PWData oldData = original.getData(PWAttachments.PW_DATA);
        PWData newData = newPlayer.getData(PWAttachments.PW_DATA);

        Map<ResourceLocation, Map<ResourceLocation, Boolean>> allUnlockData =
                oldData.getFormUnlockData().getAllUnlockData();

        for (Map.Entry<ResourceLocation, Map<ResourceLocation, Boolean>> riderEntry : allUnlockData.entrySet()) {
            ResourceLocation riderId = riderEntry.getKey();
            for (Map.Entry<ResourceLocation, Boolean> formEntry : riderEntry.getValue().entrySet()) {
                if (formEntry.getValue()) {
                    newData.unlockForm(riderId, formEntry.getKey());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PWData data = serverPlayer.getData(PWAttachments.PW_DATA);
        PWPacketHandler.sendToClient(
                serverPlayer,
                new PWDataSyncPacket(serverPlayer.getUUID(), data)
        );
    }

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            PWData data = serverPlayer.getData(PWAttachments.PW_DATA);

            // 如果数据被修改，记录日志以便调试
            if (data.isDirty()) {
                RideBattleParallelWorlds.LOGGER.debug("保存玩家 {} 的形态解锁数据",
                        player.getName().getString());
                data.clearDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            PWData data = serverPlayer.getData(PWAttachments.PW_DATA);

            // 确保数据已正确加载
            RideBattleParallelWorlds.LOGGER.debug("加载玩家 {} 的形态解锁数据: {}",
                    player.getName().getString(),
                    data.getFormUnlockData().getAllUnlockData());
        }
    }
}