package com.jpigeon.ridebattleparallelworlds.common.data.attachment;

import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.FormUnlockData;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card.CardData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 平行世界玩家数据（主入口）。
 * <p>
 * 下分两块：
 * <ul>
 *     <li>{@link FormUnlockData} —— 形态解锁（仅对声明了 unlock 的骑士生效）</li>
 *     <li>{@link CardData} —— 卡牌顺序 / 抽卡（龙骑系）</li>
 * </ul>
 * <p>
 * 本类只负责：dirty 追踪、对外 API 委托、序列化。
 * 具体数据结构与注册表同步逻辑由子数据类自己承担。
 */
public class PWData {

    private final FormUnlockData formUnlockData;
    private final CardData cardData;

    private transient boolean isDirty = false;

    public PWData(FormUnlockData formUnlockData, CardData cardData) {
        this.formUnlockData = formUnlockData != null ? formUnlockData : new FormUnlockData();
        this.cardData = cardData != null ? cardData : new CardData();
        // 构造时对齐一次注册表（首次创建 / 反序列化后）
        this.formUnlockData.initFromRegistry();
    }

    // ==================== Dirty 追踪 ====================

    public void markDirty() {
        this.isDirty = true;
    }

    public void clearDirty() {
        this.isDirty = false;
    }

    public boolean isDirty() {
        return isDirty;
    }

    // ==================== 形态解锁 ====================

    public boolean isFormUnlocked(ResourceLocation riderId, ResourceLocation formId) {
        return formUnlockData.isFormUnlocked(riderId, formId);
    }

    public boolean unlockForm(ResourceLocation riderId, ResourceLocation formId) {
        if (!formUnlockData.isFormUnlocked(riderId, formId)) {
            formUnlockData.unlockForm(riderId, formId);
            markDirty();
            return true;
        }
        return false;
    }

    public void lockForm(ResourceLocation riderId, ResourceLocation formId) {
        if (formUnlockData.isFormUnlocked(riderId, formId)) {
            formUnlockData.lockForm(riderId, formId);
            markDirty();
        }
    }

    public List<ResourceLocation> getUnlockedForms(ResourceLocation riderId) {
        return formUnlockData.getUnlockedForms(riderId);
    }

    public List<ResourceLocation> getLockedForms(ResourceLocation riderId) {
        return formUnlockData.getLockedForms(riderId);
    }

    public Map<ResourceLocation, Boolean> getRiderUnlockStatus(ResourceLocation riderId) {
        return formUnlockData.getRiderUnlockStatus(riderId);
    }

    /**
     * 直接暴露子数据（供 FormWheel / PWCommands 等只读场景使用）。
     */
    public FormUnlockData getFormUnlockData() {
        return formUnlockData;
    }

    /**
     * 从注册表刷新：委托 + dirty。
     */
    public void refreshRegisteredForms() {
        formUnlockData.refreshFromRegistry();
        markDirty();
    }

    public void resetFormUnlocksToDefaults() {
        formUnlockData.resetAllToDefaults();
        markDirty();
    }

    // ==================== 卡牌 ====================

    public CardData getCardData() {
        return cardData;
    }

    public void ensureCardOrderInitialized(ResourceLocation riderId) {
        if (cardData.ensureInitialized(riderId)) markDirty();
    }

    public void setCardOrder(ResourceLocation riderId, List<ResourceLocation> newOrder) {
        cardData.setOrder(riderId, newOrder);
        markDirty();
    }

    public List<ResourceLocation> getCardOrder(ResourceLocation riderId) {
        return cardData.getOrder(riderId);
    }

    public Set<ResourceLocation> getDrawnCards(ResourceLocation riderId) {
        return cardData.getDrawn(riderId);
    }

    /**
     * @return 抽到卡牌 id，或 {@code null} 表示已抽空。
     */
    public ResourceLocation drawNextCard(ResourceLocation riderId) {
        ResourceLocation next = cardData.peekNextCard(riderId);
        if (next == null) return null;
        if (!cardData.markDrawn(riderId, next)) return null;
        // 抽卡不持久化，无需 markDirty
        return next;
    }

    public void resetDrawnCards(ResourceLocation riderId) {
        cardData.resetDrawn(riderId);
        // drawn 不持久化
    }

    // ==================== 序列化 ====================

    public static final Codec<PWData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FormUnlockData.CODEC.optionalFieldOf("formUnlockData")
                            .forGetter(d -> Optional.of(d.formUnlockData)),
                    CardData.CODEC.optionalFieldOf("cardData")
                            .forGetter(d -> Optional.of(d.cardData))
            ).apply(instance, (formOpt, cardOpt) -> new PWData(
                    formOpt.orElseGet(FormUnlockData::new),
                    cardOpt.orElseGet(CardData::new)))
    );
}
