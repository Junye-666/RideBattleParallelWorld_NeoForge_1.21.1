package com.jpigeon.ridebattleparallelworlds.common.rider;

import com.jpigeon.rideevolutionlib.util.state.StateFlagManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

/**
 * 所有技能相关的 {@link StateFlagManager} flag 定义。
 */
public final class RiderSkillFlags {
    private RiderSkillFlags() {
    }

    // ==================== 行为标志 ====================

    /**
     * 玩家正处于骑踢判定窗口（AABB 检测、命中消费都看它）
     */
    public static final ResourceLocation KICKING = id("kick_active");

    // ==================== 踢击类技能标志 ====================

    public static final ResourceLocation GROWING_KICK = RiderSkills.GROWING_KICK;
    public static final ResourceLocation MIGHTY_KICK = RiderSkills.MIGHTY_KICK;
    public static final ResourceLocation RISING_MIGHTY_KICK = RiderSkills.RISING_MIGHTY_KICK;
    public static final ResourceLocation AMAZING_MIGHTY_KICK = RiderSkills.AMAZING_MIGHTY_KICK;
    public static final ResourceLocation ULTIMATE_KICK = RiderSkills.ULTIMATE_KICK;
    public static final ResourceLocation GROUND_KICK = RiderSkills.GROUND_KICK;

    /**
     * 踢击 flag → 命中伤害半径
     */
    public static final Map<ResourceLocation, Float> KICK_RADIUS = Map.of(
            GROWING_KICK, 2f,
            MIGHTY_KICK, 3f,
            GROUND_KICK, 3f,
            RISING_MIGHTY_KICK, 4f,
            AMAZING_MIGHTY_KICK, 5f,
            ULTIMATE_KICK, 7f
    );

    // ==================== 武器/近战类技能标志 ====================

    public static final ResourceLocation MIGHTY_PUNCH = RiderSkills.MIGHTY_PUNCH;
    public static final ResourceLocation SABER_SLASH = RiderSkills.SABER_SLASH;
    public static final ResourceLocation HALBERD_SPIN = RiderSkills.HALBERD_SPIN;
    public static final ResourceLocation FIRESTORM_ATTACK = RiderSkills.FIRESTORM_ATTACK;
    public static final ResourceLocation BURNING_BOMBER = RiderSkills.BURNING_BOMBER;

    // ==================== 便捷方法 ====================

    /**
     * 应用一个技能 flag，TTL 由调用方给
     */
    public static void apply(Player player, ResourceLocation flag, int ttlTicks) {
        StateFlagManager.apply(player, flag, ttlTicks);
    }

    public static boolean isActive(Player player, ResourceLocation flag) {
        return StateFlagManager.isActive(player, flag);
    }

    public static void remove(Player player, ResourceLocation flag) {
        StateFlagManager.remove(player, flag);
    }

    public static void clearAll(Player player) {
        StateFlagManager.clearAll(player);
    }

    /**
     * 返回当前活跃的踢击 flag（0 或 1 个）。
     * <p>
     * 语义上不可能同时两个 kick flag 活跃——如果有，返回第一个遇到的。
     */
    public static @Nullable ResourceLocation currentKickFlag(Player player) {
        for (ResourceLocation flag : KICK_RADIUS.keySet()) {
            if (StateFlagManager.isActive(player, flag)) return flag;
        }
        return null;
    }
}
