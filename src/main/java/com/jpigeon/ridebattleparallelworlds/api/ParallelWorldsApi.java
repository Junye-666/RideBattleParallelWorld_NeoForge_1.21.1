package com.jpigeon.ridebattleparallelworlds.api;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.core.network.PWPacketHandler;
import com.jpigeon.ridebattleparallelworlds.core.network.packet.PWDataSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParallelWorldsApi {
    private ParallelWorldsApi() {
    }

    /**
     * 解锁形态（通用）
     */
    public static boolean unlockForm(ServerPlayer player, ResourceLocation riderId, ResourceLocation formId) {
        PWData data = player.getData(PWAttachments.PW_DATA);

        if (data.isFormUnlocked(riderId, formId)) {
            return false;
        }

        // 解锁形态
        boolean success = data.unlockForm(riderId, formId);
        if (success) {
            // 设置数据回玩家以触发保存
            player.setData(PWAttachments.PW_DATA, data);

            // 同步到客户端
            syncPlayerData(player);

            RideBattleParallelWorlds.LOGGER.debug("解锁形态: 玩家={}, 骑士={}, 形态={}",
                    player.getName().getString(), riderId, formId);
        }

        return success;
    }

    /**
     * 解锁多个形态
     */
    public static int unlockForms(ServerPlayer player, ResourceLocation riderId,
                                  Collection<ResourceLocation> formIds) {
        int unlockedCount = 0;
        for (ResourceLocation formId : formIds) {
            if (unlockForm(player, riderId, formId)) {
                unlockedCount++;
            }
        }
        return unlockedCount;
    }

    /**
     * 检查形态是否已解锁
     */
    public static boolean isFormUnlocked(Player player, ResourceLocation riderId, ResourceLocation formId) {
        PWData data = player.getData(PWAttachments.PW_DATA);
        return data.isFormUnlocked(riderId, formId);
    }

    /**
     * 获取骑士已解锁的形态列表
     * @param player 玩家
     * @param riderId 骑士ID
     * @return 已解锁的形态ID列表
     */
    public static List<ResourceLocation> getUnlockedForms(Player player, ResourceLocation riderId) {
        if (player == null || riderId == null) {
            return Collections.emptyList();
        }

        PWData data = player.getData(PWAttachments.PW_DATA);

        return data.getUnlockedForms(riderId);
    }

    /**
     * 获取骑士未解锁的形态列表
     */
    public static List<ResourceLocation> getLockedForms(Player player, ResourceLocation riderId) {
        if (player == null || riderId == null) {
            return Collections.emptyList();
        }

        PWData data = player.getData(PWAttachments.PW_DATA);
        if (data == null) {
            return Collections.emptyList();
        }

        return data.getLockedForms(riderId);
    }

    /**
     * 获取骑士的解锁状态映射
     */
    public static Map<ResourceLocation, Boolean> getRiderUnlockStatus(Player player, ResourceLocation riderId) {
        if (player == null || riderId == null) {
            return Collections.emptyMap();
        }

        PWData data = player.getData(PWAttachments.PW_DATA);
        return data.getRiderUnlockStatus(riderId);
    }

    /**
     * 同步玩家数据
     */
    public static void syncPlayerData(ServerPlayer player) {
        PWData data = player.getData(PWAttachments.PW_DATA);
        PWPacketHandler.sendToClient(player, new PWDataSyncPacket(player.getUUID(), data));
    }
}
