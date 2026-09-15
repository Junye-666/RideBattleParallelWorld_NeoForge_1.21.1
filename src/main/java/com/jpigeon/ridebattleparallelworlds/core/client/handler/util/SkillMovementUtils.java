package com.jpigeon.ridebattleparallelworlds.core.client.handler.util;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.util.ScheduleUtils;
import com.jpigeon.ridebattleparallelworlds.core.client.ClientUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SkillMovementUtils {
    // 骑士踢逻辑辅助
    public static void riderKickJump(Player player, double jumpHeight, int ticks) {
        if (player == null) return;
        Vec3 currentMovement = player.getDeltaMovement();
        Vec3 jump = new Vec3(currentMovement.x, currentMovement.y + jumpHeight, currentMovement.z);
        RideBattleAPI.scheduleTicks(ticks, () -> addDeltaMovement(player, jump));
    }

    public static void riderKickJump(Player player, double jumpHeight) {
        riderKickJump(player, jumpHeight, 0);
    }

    public static void riderKickForward(Player player, double norm, int ticks) {
        if (player == null) return;
        Vec3 lookVec = player.getLookAngle();
        Vec3 movement = player.getDeltaMovement();
        Vec3 kick = new Vec3(
                movement.x + lookVec.x * norm * 1.5,
                movement.y + lookVec.y * norm,
                movement.z + lookVec.z * norm * 1.5
        );
        RideBattleAPI.scheduleTicks(ticks, () -> addDeltaMovement(player, kick));
    }

    public static void riderKickForward(Player player, double norm) {
        riderKickForward(player, norm, 0);
    }

    // 玩家移动辅助
    public static void addDeltaMovement(Player player, double x, double y, double z) {
        if (player instanceof LocalPlayer localPlayer) {
            ClientUtils.deplacePlayer(localPlayer, x, y, z, "add");
        }
    }

    public static void addDeltaMovement(Player player, Vec3 movement) {
        addDeltaMovement(player, movement.x(), movement.y(), movement.z());
    }

    public static void setDeltaMovement(Player player, double x, double y, double z) {
        if (player instanceof LocalPlayer localPlayer) {
            ClientUtils.deplacePlayer(localPlayer, x, y, z, "set");
        }
    }

    public static void setDeltaMovement(Player player, Vec3 movement) {
        setDeltaMovement(player, movement.x(), movement.y(), movement.z());
    }

    public static void kickRecoil(Player player) {
        Vec3 angle = player.getLookAngle();
        Vec3 current = player.getKnownMovement();
        Vec3 back = new Vec3(-(angle.x * current.x), 0.5, -(angle.z * current.z));
        setDeltaMovement(player, 0, 0, 0);
        addDeltaMovement(player, back);
    }

    public static void kickRecoilIn(Player player, int ticks) {
        ScheduleUtils.getInstance().scheduleTask(ticks, () -> kickRecoil(player));
    }
}
