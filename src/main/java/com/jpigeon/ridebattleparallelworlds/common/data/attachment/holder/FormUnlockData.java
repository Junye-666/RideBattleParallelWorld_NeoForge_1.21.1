package com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.registry.RiderRegistry;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWUnlockRegistry;
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

    public FormUnlockData() {
        this.unlockTable = HashBasedTable.create();
    }

    public FormUnlockData(Map<ResourceLocation, Map<ResourceLocation, Boolean>> existingData) {
        this.unlockTable = HashBasedTable.create();
        if (existingData != null) {
            for (var riderEntry : existingData.entrySet()) {
                for (var formEntry : riderEntry.getValue().entrySet()) {
                    unlockTable.put(riderEntry.getKey(), formEntry.getKey(), formEntry.getValue());
                }
            }
        }
    }

    // ==================== 注册表同步 ====================

    /**
     * 按当前 PWUnlockRegistry + RiderRegistry 初始化。
     * 仅对尚未收录的骑士填入，已存在的骑士保持原样。
     * <p>
     * 由 PWData 在构造时调用一次。
     */
    public void initFromRegistry() {
        for (ResourceLocation riderId : PWUnlockRegistry.getAllUnlockRiders()) {
            if (containsRider(riderId)) continue;
            RiderConfig config = RiderRegistry.getRider(riderId);
            if (config == null) continue;
            registerFromConfig(riderId, config,
                    PWUnlockRegistry.getDefaultUnlocked(riderId));
        }
    }

    /**
     * 按最新注册表刷新形态列表，保留玩家当前的解锁状态。
     * 只处理声明了 unlock 系统的骑士。
     */
    public void refreshFromRegistry() {
        // 1. 快照当前已解锁集合
        Map<ResourceLocation, Set<ResourceLocation>> snapshot = new HashMap<>();
        for (var riderEntry : getAllUnlockData().entrySet()) {
            Set<ResourceLocation> unlocked = new HashSet<>();
            for (var formEntry : riderEntry.getValue().entrySet()) {
                if (formEntry.getValue()) unlocked.add(formEntry.getKey());
            }
            snapshot.put(riderEntry.getKey(), unlocked);
        }

        // 2. 全部置为锁定
        clearAll();

        // 3. 从注册表重新写入，保留快照里的解锁状态
        for (ResourceLocation riderId : PWUnlockRegistry.getAllUnlockRiders()) {
            RiderConfig config = RiderRegistry.getRider(riderId);
            if (config == null) continue;
            Set<ResourceLocation> keep = snapshot.getOrDefault(
                    riderId, PWUnlockRegistry.getDefaultUnlocked(riderId));
            registerFromConfig(riderId, config, keep);
        }
    }

    /**
     * 用外部快照替换全部数据。用于玩家克隆（死亡重生）等场景。
     */
    public void replaceAll(Map<ResourceLocation, Map<ResourceLocation, Boolean>> all) {
        clearAll();
        for (var riderEntry : all.entrySet()) {
            for (var formEntry : riderEntry.getValue().entrySet()) {
                if (formEntry.getValue()) {
                    unlockForm(riderEntry.getKey(), formEntry.getKey());
                }
            }
        }
    }

    /**
     * 把所有已注册骑士的形态置为锁定（不删除表结构）。
     */
    public void clearAll() {
        for (ResourceLocation riderId : getAllUnlockData().keySet()) {
            lockAllRiderForms(riderId);
        }
    }

    // ==================== 内部辅助 ====================

    private void registerFromConfig(ResourceLocation riderId, RiderConfig config,
                                    Collection<ResourceLocation> defaultUnlocked) {
        List<ResourceLocation> allForms = new ArrayList<>(config.getForms().keySet());
        registerRiderForms(riderId, allForms, new ArrayList<>(defaultUnlocked));
    }

    /**
     * 注册骑士的形态列表
     *
     * @param riderId         骑士ID
     * @param allForms        该骑士的所有形态ID
     * @param defaultUnlocked 默认解锁的形态（可为空）
     */
    public void registerRiderForms(ResourceLocation riderId,
                                   List<ResourceLocation> allForms,
                                   List<ResourceLocation> defaultUnlocked) {
        if (riderId == null || allForms == null) return;
        Set<ResourceLocation> defaultSet = new HashSet<>(
                defaultUnlocked != null ? defaultUnlocked : List.of());
        for (ResourceLocation formId : allForms) {
            unlockTable.put(riderId, formId, defaultSet.contains(formId));
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

    public boolean containsRider(ResourceLocation riderId) {
        return unlockTable.containsRow(riderId);
    }

    // ==================== 数据导出 ====================

    /**
     * 获取所有解锁数据的只读视图
     */
    public Map<ResourceLocation, Map<ResourceLocation, Boolean>> getAllUnlockData() {
        return Collections.unmodifiableMap(unlockTable.rowMap());
    }

    /**
     * 把某骑士的所有形态恢复到 PWUnlockRegistry 声明的默认解锁状态。
     * 未知骑士不做任何事。
     */
    public void resetToDefaults(ResourceLocation riderId) {
        if (!PWUnlockRegistry.usesUnlock(riderId)) return;
        RiderConfig config = RiderRegistry.getRider(riderId);
        if (config == null) return;
        registerFromConfig(riderId, config, PWUnlockRegistry.getDefaultUnlocked(riderId));
    }

    /**
     * 把所有启用 unlock 的骑士重置到默认状态。
     */
    public void resetAllToDefaults() {
        for (ResourceLocation riderId : PWUnlockRegistry.getAllUnlockRiders()) {
            resetToDefaults(riderId);
        }
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
