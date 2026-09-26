package com.jpigeon.ridebattleparallelworlds.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.server.event.ReturnItemsEvent;
import com.jpigeon.ridebattlelib.server.event.SlotExtractionEvent;
import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.common.registry.ItemFormUtils;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.armor.ArcleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FormWheel {
    private static final Map<UUID, Map<ResourceLocation, Integer>> currentIndex = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onReturnItem(ReturnItemsEvent.Post event) {
        RiderConfig config = event.getConfig();
        if (config != RiderConfig.findActiveDriverConfig(event.getPlayer())) return;
        if (config.equals(KuugaConfig.KUUGA) || config.equals(AgitoConfig.AGITO)) {
            Player player = event.getPlayer();
            if (player == null || !player.isCrouching()) return;
            handleRotate(player);
        }
    }

    @SubscribeEvent
    public static void onExtract(SlotExtractionEvent.Pre event) {
        ResourceLocation slotId = event.getSlotId();
        if (slotId.equals(KuugaConfig.ARCLE_CORE) || slotId.equals(AgitoConfig.ALTER_RING_CORE)) {
            event.setAir();
        }
    }

    public static int getCurrentIndex(UUID playerId, ResourceLocation riderId) {
        return currentIndex
                .computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
                .getOrDefault(riderId, 0);
    }

    private static void setCurrentIndex(UUID playerId, ResourceLocation riderId, int index) {
        currentIndex
                .computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
                .put(riderId, index);
    }

    public static void handleRotate(Player player) {
        UUID playerId = player.getUUID();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof ArcleItem) {
            // 获取所有已解锁形态
            List<ResourceLocation> unlockedForms = ParallelWorldsApi.getUnlockedForms(player, RiderIds.KUUGA_ID);

            if (unlockedForms.isEmpty()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.displayClientMessage(
                            Component.literal("没有可用的解锁形态").withStyle(ChatFormatting.RED), true);
                }
                return;
            }

            // 只在已解锁形态中循环
            int currentIdx = getCurrentIndex(playerId, RiderIds.KUUGA_ID);
            int nextIndex = (currentIdx + 1) % unlockedForms.size();
            setCurrentIndex(playerId, RiderIds.KUUGA_ID, nextIndex);

            ResourceLocation newId = unlockedForms.get(nextIndex);
            // 显示形态名称
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(getDisplayName(newId), true);
            }
            // 更新腰带槽
            setArcleSlot(player, newId);

        } else if (legs.getItem() instanceof AlterRingItem) {
            if (player.level().isClientSide()) {
                Minecraft.getInstance().getSoundManager().stop();
            }
            List<ResourceLocation> unlockedForms =
                    ParallelWorldsApi.getUnlockedForms(player, RiderIds.AGITO_ID);

            if (unlockedForms.isEmpty()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.displayClientMessage(
                            Component.literal("没有可用的解锁形态").withStyle(ChatFormatting.RED), true);
                }
                return;
            }

            int currentIdx = getCurrentIndex(playerId, RiderIds.AGITO_ID);
            int nextIndex = (currentIdx + 1) % unlockedForms.size();
            setCurrentIndex(playerId, RiderIds.AGITO_ID, nextIndex);

            ResourceLocation newId = unlockedForms.get(nextIndex);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(getDisplayName(newId), true);
            }
            setAlterRingSlot(player, newId);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        currentIndex.remove(event.getEntity().getUUID());
    }

    private static Component getDisplayName(ResourceLocation newId) {
        if (newId == null) {
            return Component.literal("为null的形态？");
        }
        return Component.translatable("form.ridebattleparallelworlds." + newId.getPath().toLowerCase())
                .withStyle(getChatFormatting(newId));
    }

    private static ChatFormatting getChatFormatting(ResourceLocation formId) {
        if (formId == null) return ChatFormatting.BOLD;

        String string = formId.getPath();
        if (string.contains("amazing") || string.contains("ultimate")) {
            return ChatFormatting.BLACK;
        } else if (string.contains("mighty") || string.contains("flame") || string.contains("burning")) {
            return ChatFormatting.RED;
        } else if (string.contains("dragon") || string.contains("storm")) {
            return ChatFormatting.DARK_AQUA;
        } else if (string.contains("pegasus")) {
            return ChatFormatting.DARK_GREEN;
        } else if (string.contains("titan")) {
            return ChatFormatting.DARK_PURPLE;
        } else if (string.contains("ground")) {
            return ChatFormatting.GOLD;
        }
        return ChatFormatting.BOLD;
    }

    public static void setArcleSlot(Player player, ResourceLocation formId) {
        setDriverSlotFor(player, KuugaConfig.ARCLE_CORE, formId);
    }

    public static void setAlterRingSlot(Player player, ResourceLocation formId) {
        setDriverSlotFor(player, AgitoConfig.ALTER_RING_CORE, formId);
    }

    private static void setDriverSlotFor(Player player, ResourceLocation coreSlotId, ResourceLocation formId) {
        if (player == null || formId == null) return;
        Item item = ItemFormUtils.getItemForForm(formId);
        if (item == null) return;
        RideBattleAPI.insertItemToSlot(player, coreSlotId, item.getDefaultInstance());
    }
}

