package com.jpigeon.ridebattleparallelworlds.common.data.attachment;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class PWUnlockRegistry {
    private PWUnlockRegistry() {}

    private static final Map<ResourceLocation, Set<ResourceLocation>> UNLOCK_CONFIG = new HashMap<>();

    /** 声明此骑士使用解锁系统，并给出默认解锁的形态集合。 */
    public static void enableUnlock(ResourceLocation riderId, Collection<ResourceLocation> defaultUnlocked) {
        UNLOCK_CONFIG.put(riderId, Set.copyOf(defaultUnlocked));
    }

    public static boolean usesUnlock(ResourceLocation riderId) {
        return UNLOCK_CONFIG.containsKey(riderId);
    }

    /**
     * 返回此骑士默认解锁的形态集合。
     * <p>
     * - 未启用解锁系统 → 返回空集合
     * - 已启用 → 返回不可变快照
     */
    public static Set<ResourceLocation> getDefaultUnlocked(ResourceLocation riderId) {
        return UNLOCK_CONFIG.getOrDefault(riderId, Set.of());
    }

    public static Set<ResourceLocation> getAllUnlockRiders() {
        return Collections.unmodifiableSet(UNLOCK_CONFIG.keySet());
    }
}