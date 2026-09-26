package com.jpigeon.ridebattleparallelworlds.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.holder.card.CardDeckRegistry;
import com.jpigeon.ridebattleparallelworlds.common.event.CardDrawnEvent;
import com.jpigeon.ridebattleparallelworlds.common.network.packet.DeckPackets;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item.MirrorDeckItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public final class MirrorDeckHandler {
    private MirrorDeckHandler() {
    }

    /**
     * 打开卡牌编排界面。
     * <p>
     * 只要玩家能访问这个卡池就能打开：
     * - 未变身：主手或副手持有 formId 匹配的 MirrorDeckItem
     * - 已变身：当前形态就是 formId
     */
    public static void onOpen(Player player, ResourceLocation formId) {
        if (!(player instanceof ServerPlayer sp)) return;
        if (CardDeckRegistry.getCards(formId).isEmpty()) return;
        if (!canAccessDeck(player, formId)) return;

        PWData data = player.getData(PWAttachments.PW_DATA);
        data.ensureCardOrderInitialized(formId);
        player.setData(PWAttachments.PW_DATA, data);

        PacketDistributor.sendToPlayer(sp, new DeckPackets.DeckSyncPacket(
                formId, data.getCardOrder(formId), data.getDrawnCards(formId)));
    }

    /**
     * 保存卡牌顺序。与 onOpen 同样的访问条件。
     */
    public static void onSave(Player player, ResourceLocation formId, List<ResourceLocation> order) {
        if (!(player instanceof ServerPlayer)) return;
        if (CardDeckRegistry.getCards(formId).isEmpty()) return;
        if (!canAccessDeck(player, formId)) return;

        PWData data = player.getData(PWAttachments.PW_DATA);
        data.setCardOrder(formId, order);
        player.setData(PWAttachments.PW_DATA, data);
    }

    /**
     * 抽卡：只在变身且当前形态等于 formId 时生效。
     */
    public static void onDraw(Player player, ResourceLocation formId) {
        if (!(player instanceof ServerPlayer sp)) return;
        if (!RideBattleAPI.isSpecificForm(player, formId)) return;

        PWData data = player.getData(PWAttachments.PW_DATA);
        ResourceLocation drawn = data.drawNextCard(formId);
        if (drawn == null) {
            sp.displayClientMessage(
                    Component.translatable("message.ridebattleparallelworlds.deck_empty"),
                    true);
            return;
        }
        player.setData(PWAttachments.PW_DATA, data);

        Item cardItem = BuiltInRegistries.ITEM.get(drawn);
        if (cardItem == Items.AIR) {
            RideBattleParallelWorlds.LOGGER.warn("抽到未注册的卡牌物品: {}", drawn);
            return;
        }
        ItemStack stack = new ItemStack(cardItem);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }

        NeoForge.EVENT_BUS.post(new CardDrawnEvent(player, formId, drawn));
    }

    public static void resetDrawn(Player player, ResourceLocation formId) {
        PWData data = player.getData(PWAttachments.PW_DATA);
        data.resetDrawnCards(formId);
        player.setData(PWAttachments.PW_DATA, data);
    }

    // ==================== 内部 ====================

    private static boolean canAccessDeck(Player player, ResourceLocation formId) {
        if (RideBattleAPI.isSpecificForm(player, formId)) return true;
        return hasMatchingDeckInHand(player, formId);
    }

    private static boolean hasMatchingDeckInHand(Player player, ResourceLocation formId) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof MirrorDeckItem deck
                    && java.util.Objects.equals(deck.getFormId(), formId)) {
                return true;
            }
        }
        return false;
    }
}