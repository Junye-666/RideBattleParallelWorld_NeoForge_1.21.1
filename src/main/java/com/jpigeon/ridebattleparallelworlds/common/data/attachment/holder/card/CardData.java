package com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 玩家卡牌数据。
 * <p>
 * 只持久化 {@code cardOrders}（每个骑士一张顺序表）；
 * {@code drawnCards} 是变身期间的运行时状态，解除变身 / 重登时清空。
 */
public class CardData {

    private final Map<ResourceLocation, List<ResourceLocation>> cardOrders;
    private final Map<ResourceLocation, Set<ResourceLocation>> drawnCards;

    public CardData() {
        this.cardOrders = new HashMap<>();
        this.drawnCards = new HashMap<>();
    }

    public CardData(Map<ResourceLocation, List<ResourceLocation>> orders) {
        this.cardOrders = new HashMap<>(orders);
        this.drawnCards = new HashMap<>();
    }

    // ---------- 顺序 ----------

    public List<ResourceLocation> getOrder(ResourceLocation riderId) {
        return cardOrders.getOrDefault(riderId, CardDeckRegistry.getCards(riderId));
    }

    public boolean hasOrder(ResourceLocation riderId) {
        return cardOrders.containsKey(riderId);
    }

    /**
     * 只保留已注册的卡；缺的自动补到末尾，保持顺序稳定。
     */
    private static List<ResourceLocation> sanitize(ResourceLocation riderId,
                                                   List<ResourceLocation> source) {
        List<ResourceLocation> registered = CardDeckRegistry.getCards(riderId);
        List<ResourceLocation> out = new ArrayList<>();
        if (source != null) {
            for (ResourceLocation id : source) {
                if (registered.contains(id) && !out.contains(id)) out.add(id);
            }
        }
        for (ResourceLocation id : registered) {
            if (!out.contains(id)) out.add(id);
        }
        return out;
    }

    public void setOrder(ResourceLocation riderId, List<ResourceLocation> newOrder) {
        cardOrders.put(riderId, sanitize(riderId, newOrder));
    }

    /**
     * 确保顺序包含全部已注册卡。
     *
     * @return 是否实际发生了写入（用于 dirty 追踪）
     */
    public boolean ensureInitialized(ResourceLocation riderId) {
        List<ResourceLocation> before = cardOrders.get(riderId);
        List<ResourceLocation> after = sanitize(riderId, before);
        if (after.equals(before)) return false;
        cardOrders.put(riderId, after);
        return true;
    }

    // ---------- 抽卡 ----------

    public Set<ResourceLocation> getDrawn(ResourceLocation riderId) {
        return Collections.unmodifiableSet(drawnCards.getOrDefault(riderId, Set.of()));
    }

    public boolean isDrawn(ResourceLocation riderId, ResourceLocation cardId) {
        return drawnCards.getOrDefault(riderId, Set.of()).contains(cardId);
    }

    @Nullable
    public ResourceLocation peekNextCard(ResourceLocation riderId) {
        Set<ResourceLocation> drawn = drawnCards.getOrDefault(riderId, Set.of());
        for (ResourceLocation card : getOrder(riderId)) {
            if (!drawn.contains(card)) return card;
        }
        return null;
    }

    public boolean markDrawn(ResourceLocation riderId, ResourceLocation cardId) {
        return drawnCards.computeIfAbsent(riderId, k -> new HashSet<>()).add(cardId);
    }

    public void resetDrawn(ResourceLocation riderId) {
        drawnCards.remove(riderId);
    }

    // ---------- 复制 ----------

    /**
     * 用于玩家克隆 / 数据同步：复制持久部分，drawn 保持清空。
     */
    public void copyFrom(CardData other) {
        this.cardOrders.clear();
        this.cardOrders.putAll(other.cardOrders);
        this.drawnCards.clear();
    }

    // ---------- 序列化 ----------

    public Map<ResourceLocation, List<ResourceLocation>> getAllOrders() {
        return Collections.unmodifiableMap(cardOrders);
    }

    public static final Codec<CardData> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.unboundedMap(
                                    ResourceLocation.CODEC,
                                    ResourceLocation.CODEC.listOf()
                            ).optionalFieldOf("cardOrders", Map.of())
                            .forGetter(CardData::getAllOrders)
            ).apply(inst, CardData::new)
    );
}
