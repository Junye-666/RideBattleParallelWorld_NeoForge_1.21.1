package com.jpigeon.ridebattleparallelworlds.server.util;

import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWUnlockRegistry;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ItemData;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AbilitiesUtils {
    public static void convertItemTo(Player player, ItemStack originStack, Item targetItem) {
        ItemStack targetStack = targetItem.getDefaultInstance();
        copyItemStackData(originStack, targetStack, player);
        originStack.shrink(1);
        if (!player.getInventory().add(targetStack)) {
            player.drop(targetStack, false);
        }
        player.getCooldowns().addCooldown(targetItem, 10);
    }

    public static boolean isValidItem(ItemStack itemStack, TagKey<Item> tagKey) {
        return itemStack.is(tagKey);
    }

    public static void removeItemFromPlayer(Item toRemove, Player player) {
        Inventory inventory = player.getInventory();
        boolean changed = false;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty() || !stack.is(toRemove)) continue;

            ItemStack restored = toRestoredItem(stack, player);
            if (restored != null) {
                inventory.setItem(i, restored);
                changed = true;
            }
        }
        if (changed) inventory.setChanged();
    }

    private static int findSlot(Player player, Predicate<ItemStack> predicate) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && predicate.test(stack)) return i;
        }
        return -1;
    }

    private static int countSlots(Player player, Predicate<ItemStack> predicate) {
        Inventory inv = player.getInventory();
        int count = 0;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && predicate.test(stack)) count++;
        }
        return count;
    }

    private static void replaceItemStackWith(ItemStack original, ItemStack target, Player player) {
        int i = findSlot(player, s -> s.is(original.getItem()));
        if (i < 0) return;
        Inventory inventory = player.getInventory();
        inventory.setItem(i, target);
        inventory.setChanged();
    }

    public static ItemStack searchForItemFromPlayer(Item item, Player player) {
        int idx = findSlot(player, s -> s.is(item));
        return idx >= 0 ? player.getInventory().getItem(idx) : null;
    }

    private static ItemStack toRestoredItem(ItemStack itemStack, Player player) {
        if (itemStack.isEmpty()) return null;
        ItemData originData = itemStack.get(ModDataComponents.ORIGIN_ITEM_DATA.get());
        if (originData == null || originData.tag().isEmpty()) return null;
        return originData.toItemStack(player.registryAccess());
    }

    /**
     * 保存物品数据到目标物品
     */
    public static void copyItemStackData(ItemStack source, ItemStack target, Player player) {
        if (source.isEmpty()) return;
        ItemData data = ItemData.fromItemStack(source, player.registryAccess());
        target.set(ModDataComponents.ORIGIN_ITEM_DATA.get(), data);
    }

    public static boolean hasItemInInventory(Item item, Player player) {
        return findSlot(player, s -> s.is(item)) >= 0;
    }

    public static boolean hasItemInInventory(Item item, Player player, int count) {
        return countSlots(player, s -> s.is(item)) == count;
    }

    public static void transferData(ItemStack originItem, ItemStack targetItem, Player player) {
        if (originItem.isEmpty() || targetItem.isEmpty()) {
            return;
        }
        ItemData originData = originItem.get(ModDataComponents.ORIGIN_ITEM_DATA.get());
        targetItem.set(ModDataComponents.ORIGIN_ITEM_DATA.get(), originData);
        replaceItemStackWith(originItem, targetItem, player);
    }

    public static void unlockFormIfLocked(Player player, ResourceLocation riderId, ResourceLocation formId) {
        if (!PWUnlockRegistry.usesUnlock(riderId)) return;
        if (!ParallelWorldsApi.isFormUnlocked(player, riderId, formId)) {
            if (player instanceof ServerPlayer serverPlayer) {
                ParallelWorldsApi.unlockForm(serverPlayer, riderId, formId);
            }
        }
    }
}