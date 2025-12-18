package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.ReturnItemsEvent;
import com.jpigeon.ridebattlelib.core.system.event.SlotExtractionEvent;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

public class FormWheel {
    @SubscribeEvent
    public static void onReturnItem(ReturnItemsEvent.Pre event) {
        if (event.isCanceled()) return;
        if (event.getConfig().equals(KuugaConfig.KUUGA)) {
            Player player = event.getPlayer();
            if (player == null || !player.isCrouching()) return;
            handleRotate(player);
        }
    }

    public static List<ResourceLocation> KUUGA_FORM_ID_WHEEL = List.of(
            KuugaConfig.GROWING_ID,
            KuugaConfig.MIGHTY_ID,
            KuugaConfig.DRAGON_ID,
            KuugaConfig.PEGASUS_ID,
            KuugaConfig.TITAN_ID,
            KuugaConfig.RISING_MIGHTY_ID,
            KuugaConfig.RISING_DRAGON_ID,
            KuugaConfig.RISING_PEGASUS_ID,
            KuugaConfig.RISING_TITAN_ID,
            KuugaConfig.AMAZING_MIGHTY_ID,
            KuugaConfig.ULTIMATE_ID
    );

    private static int currentIndex = 0;

    private static int getCurrentIndex() {
        return currentIndex;
    }

    private static void setCurrentIndex(int currentIndex) {
        FormWheel.currentIndex = currentIndex;
    }

    public static void handleRotate(Player player) {
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof ArcleItem) {
            int currentIndex = getCurrentIndex();
            int maxIndex = KUUGA_FORM_ID_WHEEL.size();
            // 循环切换技能
            int newIndex = (currentIndex + 1) % maxIndex;
            setCurrentIndex(newIndex);

            // 获取新技能的ID
            ResourceLocation newId = KUUGA_FORM_ID_WHEEL.get(newIndex);
            Component displayName =
                    Component.translatable("form.ridebattleparallelworlds." + newId.getPath().toLowerCase())
                            .withStyle(getChatFormatting(newId));

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(displayName, true);
            }

            RiderManager.extractItemFromSlot(player, KuugaConfig.ARCLE_CORE);
            RiderHandler.setDriverAnim(legs, newId);
            RiderManager.scheduleTicks(5, () -> setArcleSlot(player, newId));
        }
    }

    private static ChatFormatting getChatFormatting(ResourceLocation riderId) {
        String string = riderId.toString();
        if (string.contains("amazing") || string.contains("ultimate")) {
            return ChatFormatting.BLACK;
        } else if (string.contains("mighty")) {
            return ChatFormatting.RED;
        } else if (string.contains("dragon")) {
            return ChatFormatting.DARK_AQUA;
        } else if (string.contains("pegasus")) {
            return ChatFormatting.DARK_GREEN;
        } else if (string.contains("titan")) {
            return ChatFormatting.DARK_PURPLE;
        }
        return ChatFormatting.BOLD;
    }

    @SubscribeEvent
    public static void onExtract(SlotExtractionEvent.Pre event) {
        if (event.getSlotId().equals(KuugaConfig.ARCLE_CORE)) {
            event.setExtractedStack(Items.AIR);
        }
    }

    private static void setArcleSlot(Player player, ResourceLocation formId) {
        ResourceLocation arcleCore = KuugaConfig.ARCLE_CORE;
        if (formId.equals(KuugaConfig.MIGHTY_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.MIGHTY_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.DRAGON_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.DRAGON_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.PEGASUS_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.PEGASUS_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.TITAN_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.TITAN_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.RISING_MIGHTY_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.RISING_MIGHTY_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.RISING_DRAGON_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.RISING_DRAGON_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.RISING_PEGASUS_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.RISING_TITAN_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.RISING_TITAN_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.AMAZING_MIGHTY_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.AMAZING_MIGHTY_ELEMENT.get());
        } else if (formId.equals(KuugaConfig.ULTIMATE_ID)) {
            RiderManager.insertItemToSlot(player, arcleCore, ModItems.ULTIMATE_ELEMENT.get());
        }
    }
}
