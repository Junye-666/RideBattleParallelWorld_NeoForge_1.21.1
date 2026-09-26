package com.jpigeon.ridebattleparallelworlds.common.data.attachment;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.FormDataSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class PWAttachmentsHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PWData data = serverPlayer.getData(PWAttachments.PW_DATA);
        // 登录时对齐一次注册表
        data.getFormUnlockData().initFromRegistry();
        serverPlayer.setData(PWAttachments.PW_DATA, data);

        PacketDistributor.sendToPlayer(
                serverPlayer,
                new FormDataSyncPacket(serverPlayer.getUUID(), data)
        );
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        PWData oldData = original.getData(PWAttachments.PW_DATA);
        PWData newData = newPlayer.getData(PWAttachments.PW_DATA);

        // 形态解锁
        newData.getFormUnlockData().replaceAll(
                oldData.getFormUnlockData().getAllUnlockData());

        // 卡牌顺序
        newData.getCardData().copyFrom(oldData.getCardData());

        newPlayer.setData(PWAttachments.PW_DATA, newData);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PWData data = serverPlayer.getData(PWAttachments.PW_DATA);
        data.getFormUnlockData().initFromRegistry();
        serverPlayer.setData(PWAttachments.PW_DATA, data);

        PacketDistributor.sendToPlayer(
                serverPlayer,
                new FormDataSyncPacket(serverPlayer.getUUID(), data)
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
