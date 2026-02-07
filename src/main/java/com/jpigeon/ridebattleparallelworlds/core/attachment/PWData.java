package com.jpigeon.ridebattleparallelworlds.core.attachment;

import com.jpigeon.ridebattleparallelworlds.core.riders.RiderForms;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * 平行世界玩家数据
 * 整合了所有骑士的形态解锁数据
 */
public class PWData {
    private final FormUnlockData formUnlockData;

    // 添加一个标记，用于追踪数据是否被修改
    private transient boolean isDirty = false;

    public PWData() {
        this(new FormUnlockData());
    }

    public PWData(FormUnlockData formUnlockData) {
        this.formUnlockData = formUnlockData != null ? formUnlockData : new FormUnlockData();
        initializeRiderForms();
    }

    private void initializeRiderForms() {
        // 检查是否已经有数据，避免覆盖
        if (formUnlockData.getRiderUnlockStatus(RiderIds.KUUGA_ID).isEmpty()) {
            formUnlockData.registerRiderForms(
                    RiderIds.KUUGA_ID,
                    RiderForms.KUUGA_FORMS,
                    List.of(KuugaConfig.GROWING_ID)
            );
        }
        if (formUnlockData.getRiderUnlockStatus(RiderIds.AGITO_ID).isEmpty()) {
            formUnlockData.registerRiderForms(
                    RiderIds.AGITO_ID,
                    RiderForms.AGITO_FORMS,
                    List.of(AgitoConfig.GROUND_ID)
            );
        }
    }

    public void reloadFormUnlockData() {
        List<ResourceLocation> KUUGA_FORMS = formUnlockData.getUnlockedForms(RiderIds.KUUGA_ID);
        List<ResourceLocation> AGITO_FORMS = formUnlockData.getUnlockedForms(RiderIds.AGITO_ID);
        clearAllFormUnlockData();
        formUnlockData.registerRiderForms(
                RiderIds.KUUGA_ID,
                RiderForms.KUUGA_FORMS,
                KUUGA_FORMS
        );
        formUnlockData.registerRiderForms(
                RiderIds.AGITO_ID,
                RiderForms.AGITO_FORMS,
                AGITO_FORMS
        );
    }

    public boolean isFormUnlocked(ResourceLocation riderId, ResourceLocation formId) {
        return formUnlockData.isFormUnlocked(riderId, formId);
    }

    public boolean unlockForm(ResourceLocation riderId, ResourceLocation formId) {
        // 只有在状态改变时才标记为脏
        if (!isFormUnlocked(riderId, formId)) {
            formUnlockData.unlockForm(riderId, formId);
            markDirty();
            return true;
        }
        return false;
    }

    public void lockForm(ResourceLocation riderId, ResourceLocation formId) {
        if (isFormUnlocked(riderId, formId)) {
            formUnlockData.lockForm(riderId, formId);
            markDirty();
        }
    }

    // 标记数据为脏，需要保存
    public void markDirty() {
        this.isDirty = true;
    }

    // 清除脏标记
    public void clearDirty() {
        this.isDirty = false;
    }

    // 检查是否需要保存
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * 获取骑士已解锁的形态列表
     */
    public List<ResourceLocation> getUnlockedForms(ResourceLocation riderId) {
        return formUnlockData.getUnlockedForms(riderId);
    }

    /**
     * 获取骑士未解锁的形态列表
     */
    public List<ResourceLocation> getLockedForms(ResourceLocation riderId) {
        return formUnlockData.getLockedForms(riderId);
    }

    /**
     * 获取骑士的解锁状态映射
     */
    public Map<ResourceLocation, Boolean> getRiderUnlockStatus(ResourceLocation riderId) {
        return formUnlockData.getRiderUnlockStatus(riderId);
    }

    // 获取 FormUnlockData
    public FormUnlockData getFormUnlockData() {
        return formUnlockData;
    }

    // 清空所有解锁数据
    public void clearAllFormUnlockData() {
        // 获取所有已注册的骑士
        Map<ResourceLocation, Map<ResourceLocation, Boolean>> allData = formUnlockData.getAllUnlockData();
        for (ResourceLocation riderId : allData.keySet()) {
            formUnlockData.lockAllRiderForms(riderId);
        }
    }



    // ==================== 序列化 ====================

    public static final Codec<PWData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FormUnlockData.CODEC.optionalFieldOf("formUnlockData", new FormUnlockData())
                            .forGetter(data -> data.formUnlockData)
            ).apply(instance, PWData::new)
    );
}