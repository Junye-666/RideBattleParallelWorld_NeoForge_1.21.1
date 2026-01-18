package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.core.system.event.ReturnItemsEvent;
import com.jpigeon.ridebattlelib.core.system.event.SlotExtractionEvent;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.network.handler.PacketHandler;
import com.jpigeon.ridebattlelib.core.system.network.packet.InsertItemPacket;
import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderForms;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormWheel {
    private static final Map<List<ResourceLocation>, Integer> currentIndex = new HashMap<>();

    static {
        currentIndex.put(RiderForms.KUUGA_FORMS, 0);
        currentIndex.put(RiderForms.AGITO_FORMS, 0);
    }

    @SubscribeEvent
    public static void onReturnItem(ReturnItemsEvent.Pre event) {
        if (event.isCanceled()) return;
        RiderConfig config = event.getConfig();
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
            event.setExtractedStack(Items.AIR);
        }
    }

    public static int getCurrentIndex(List<ResourceLocation> list) {
        return currentIndex.getOrDefault(list, 0);
    }

    private static void setCurrentIndex(List<ResourceLocation> list, int index) {
        currentIndex.put(list, index);
    }

    public static void handleRotate(Player player) {
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        Minecraft.getInstance().getSoundManager().stop();
        if (legs.getItem() instanceof ArcleItem) {
            ResourceLocation kuuga = RiderIds.KUUGA_ID;

            // 获取所有已解锁形态
            List<ResourceLocation> unlockedForms = ParallelWorldsApi.getUnlockedForms(player, kuuga);

            if (unlockedForms.isEmpty()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.displayClientMessage(
                            Component.literal("没有可用的解锁形态").withStyle(ChatFormatting.RED),
                            true
                    );
                }
                return;
            }

            // 只在已解锁形态中循环
            int currentIdx = getCurrentIndex(unlockedForms);
            int nextIndex = (currentIdx + 1) % unlockedForms.size();
            setCurrentIndex(unlockedForms, nextIndex);

            ResourceLocation newId = unlockedForms.get(nextIndex);

            // 显示形态名称
            Component displayName = getDisplayName(newId);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(displayName, true);
            }

            // 更新腰带槽
            setArcleSlot(player, newId);
            RiderHandler.setDriverAnim(legs, newId);
        } else if (legs.getItem() instanceof AlterRingItem) {
            ResourceLocation agito = RiderIds.AGITO_ID;
            List<ResourceLocation> unlockedForms = ParallelWorldsApi.getUnlockedForms(player, agito);
            if (unlockedForms.isEmpty()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.displayClientMessage(
                            Component.literal("没有可用的解锁形态").withStyle(ChatFormatting.RED),
                            true
                    );
                }
                return;
            }
            int currentIdx = getCurrentIndex(unlockedForms);
            int nextIndex = (currentIdx + 1) % unlockedForms.size();
            setCurrentIndex(unlockedForms, nextIndex);
            ResourceLocation newId = unlockedForms.get(nextIndex);

            Component displayName = getDisplayName(newId);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(displayName, true);
            }

            setAlterRingSlot(player, newId);
            RiderHandler.setDriverAnim(legs, newId);
        }
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
        } else if (string.contains("mighty") || string.contains("flame")) {
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
        if (formId == null || player == null) return;

        ResourceLocation arcleCore = KuugaConfig.ARCLE_CORE;
        Item item = null;

        if (formId.equals(KuugaConfig.MIGHTY_ID)) {
            item = ModItems.MIGHTY_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.DRAGON_ID)) {
            item = ModItems.DRAGON_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.PEGASUS_ID)) {
            item = ModItems.PEGASUS_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.TITAN_ID)) {
            item = ModItems.TITAN_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.RISING_MIGHTY_ID)) {
            item = ModItems.RISING_MIGHTY_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.RISING_DRAGON_ID)) {
            item = ModItems.RISING_DRAGON_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
            item = ModItems.RISING_PEGASUS_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.RISING_TITAN_ID)) {
            item = ModItems.RISING_TITAN_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.AMAZING_MIGHTY_ID)) {
            item = ModItems.AMAZING_MIGHTY_ELEMENT.get();
        } else if (formId.equals(KuugaConfig.ULTIMATE_ID)) {
            item = ModItems.ULTIMATE_ELEMENT.get();
        }

        if (item != null) {
            PacketHandler.sendToServer(new InsertItemPacket(player.getUUID(), arcleCore, item.getDefaultInstance()));
        }
    }

    public static void setAlterRingSlot(Player player, ResourceLocation formId) {
        if (formId == null || player == null) return;

        ResourceLocation alterRingCore = AgitoConfig.ALTER_RING_CORE;
        Item item = null;
        if (formId.equals(AgitoConfig.GROUND_ID)) {
            item = ModItems.GROUND_ELEMENT.get();
        } else if (formId.equals(AgitoConfig.FLAME_ID)) {
            item = ModItems.FLAME_ELEMENT.get();
        } else if (formId.equals(AgitoConfig.STORM_ID)) {
            item = ModItems.STORM_ELEMENT.get();
        }

        if (item != null) {
            PacketHandler.sendToServer(new InsertItemPacket(player.getUUID(), alterRingCore, item.getDefaultInstance()));
        }
    }
}