package com.jpigeon.ridebattleparallelworlds.core.attachment;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

/**
 * 形态解锁数据管理器
 * 支持多个骑士的形态解锁状态管理
 */
public class FormUnlockData {
    // 使用 Table<RiderId, FormId, Boolean> 存储解锁状态
    // 第一层：骑士ID，第二层：形态ID，值：是否解锁
    private final Table<ResourceLocation, ResourceLocation, Boolean> unlockTable;

    // 骑士默认解锁的形态配置
    private final Map<ResourceLocation, Set<ResourceLocation>> defaultUnlockedForms;

    public FormUnlockData() {
        this.unlockTable = HashBasedTable.create();
        this.defaultUnlockedForms = new HashMap<>();
    }

    public FormUnlockData(Map<ResourceLocation, Map<ResourceLocation, Boolean>> existingData) {
        this.unlockTable = HashBasedTable.create();
        this.defaultUnlockedForms = new HashMap<>();

        if (existingData != null) {
            for (Map.Entry<ResourceLocation, Map<ResourceLocation, Boolean>> riderEntry : existingData.entrySet()) {
                ResourceLocation riderId = riderEntry.getKey();
                for (Map.Entry<ResourceLocation, Boolean> formEntry : riderEntry.getValue().entrySet()) {
                    unlockTable.put(riderId, formEntry.getKey(), formEntry.getValue());
                }
            }
        }
    }

    // ==================== 注册方法 ====================

    /**
     * 注册骑士的形态列表
     * @param riderId 骑士ID
     * @param allForms 该骑士的所有形态ID
     * @param defaultUnlocked 默认解锁的形态（可为空）
     */
    public void registerRiderForms(ResourceLocation riderId,
                                   List<ResourceLocation> allForms,
                                   List<ResourceLocation> defaultUnlocked) {
        if (riderId == null || allForms == null) return;

        // 设置默认解锁状态
        Set<ResourceLocation> defaultSet = new HashSet<>(defaultUnlocked != null ? defaultUnlocked : List.of());
        defaultUnlockedForms.put(riderId, defaultSet);

        // 初始化解锁表
        for (ResourceLocation formId : allForms) {
            boolean isDefaultUnlocked = defaultSet.contains(formId);
            unlockTable.put(riderId, formId, isDefaultUnlocked);
        }
    }

    // ==================== 解锁状态管理 ====================

    /**
     * 检查形态是否已解锁
     */
    public boolean isFormUnlocked(ResourceLocation riderId, ResourceLocation formId) {
        Boolean unlocked = unlockTable.get(riderId, formId);
        return unlocked != null && unlocked;
    }

    /**
     * 解锁形态
     */
    public void unlockForm(ResourceLocation riderId, ResourceLocation formId) {
        if (unlockTable.contains(riderId, formId)) {
            unlockTable.put(riderId, formId, true);
        }
    }

    /**
     * 锁定形态
     */
    public void lockForm(ResourceLocation riderId, ResourceLocation formId) {
        if (unlockTable.contains(riderId, formId)) {
            unlockTable.put(riderId, formId, false);
        }
    }

    /**
     * 解锁骑士的所有形态
     */
    public void unlockAllRiderForms(ResourceLocation riderId) {
        Map<ResourceLocation, Boolean> riderForms = unlockTable.row(riderId);
        riderForms.replaceAll((formId, value) -> true);
    }

    /**
     * 锁定骑士的所有形态
     */
    public void lockAllRiderForms(ResourceLocation riderId) {
        Map<ResourceLocation, Boolean> riderForms = unlockTable.row(riderId);
        riderForms.replaceAll((formId, value) -> false);
    }

    // ==================== 批量操作 ====================

    /**
     * 解锁多个形态
     */
    public void unlockForms(ResourceLocation riderId, Collection<ResourceLocation> formIds) {
        for (ResourceLocation formId : formIds) {
            unlockForm(riderId, formId);
        }
    }

    /**
     * 锁定多个形态
     */
    public void lockForms(ResourceLocation riderId, Collection<ResourceLocation> formIds) {
        for (ResourceLocation formId : formIds) {
            lockForm(riderId, formId);
        }
    }

    // ==================== 查询方法 ====================

    /**
     * 获取骑士的所有形态解锁状态
     */
    public Map<ResourceLocation, Boolean> getRiderUnlockStatus(ResourceLocation riderId) {
        return Collections.unmodifiableMap(unlockTable.row(riderId));
    }

    /**
     * 获取骑士已解锁的形态列表
     */
    public List<ResourceLocation> getUnlockedForms(ResourceLocation riderId) {
        List<ResourceLocation> unlocked = new ArrayList<>();
        Map<ResourceLocation, Boolean> riderForms = unlockTable.row(riderId);
        for (Map.Entry<ResourceLocation, Boolean> entry : riderForms.entrySet()) {
            if (entry.getValue()) {
                unlocked.add(entry.getKey());
            }
        }
        return Collections.unmodifiableList(unlocked);
    }

    /**
     * 获取骑士未解锁的形态列表
     */
    public List<ResourceLocation> getLockedForms(ResourceLocation riderId) {
        List<ResourceLocation> locked = new ArrayList<>();
        Map<ResourceLocation, Boolean> riderForms = unlockTable.row(riderId);
        for (Map.Entry<ResourceLocation, Boolean> entry : riderForms.entrySet()) {
            if (!entry.getValue()) {
                locked.add(entry.getKey());
            }
        }
        return Collections.unmodifiableList(locked);
    }

    /**
     * 检查骑士是否至少有一个形态已解锁
     */
    public boolean hasAnyFormUnlocked(ResourceLocation riderId) {
        Map<ResourceLocation, Boolean> riderForms = unlockTable.row(riderId);

        return riderForms.values().stream().anyMatch(Boolean::booleanValue);
    }

    // ==================== 数据导出 ====================

    /**
     * 获取所有解锁数据的只读视图
     */
    public Map<ResourceLocation, Map<ResourceLocation, Boolean>> getAllUnlockData() {
        return Collections.unmodifiableMap(unlockTable.rowMap());
    }

    // ==================== 序列化 ====================

    public static final Codec<FormUnlockData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(
                            ResourceLocation.CODEC,
                            Codec.unboundedMap(ResourceLocation.CODEC, Codec.BOOL)
                    ).optionalFieldOf("unlockData", Map.of()).forGetter(FormUnlockData::getAllUnlockData)
            ).apply(instance, FormUnlockData::new)
    );
}
