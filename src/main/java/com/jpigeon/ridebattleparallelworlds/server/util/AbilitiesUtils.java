package com.jpigeon.ridebattleparallelworlds.server.util;

import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ItemData;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AbilitiesUtils {
    public static void convertItemTo(Player player, ItemStack originStack, Item targetItem) {
        ItemStack targetStack = targetItem.getDefaultInstance();
        copyItemStackData(originStack, targetStack);
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
        boolean found = false;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && stack.getItem() == toRemove) {
                found = true;

                ItemStack restoredStack = toRestoredItem(stack);

                // 移除
                if (restoredStack != null) {
                    inventory.setItem(i, restoredStack);
                }
            }
        }

        if (found) {
            inventory.setChanged();
        }
    }

    private static void replaceItemStackWith(ItemStack original, ItemStack target, Player player) {
        int i = getInventoryIndex(original.getItem(), player);
        Inventory inventory = player.getInventory();
        inventory.setItem(i, target);
        inventory.setChanged();
    }

    public static ItemStack searchForItemFromPlayer(Item item, Player player) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return stack;
            }
        }
        return null;
    }

    private static int getInventoryIndex(Item item, Player player) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return i;
            }
        }
        return 0;
    }

    private static ItemStack toRestoredItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) return null;
        ItemData originData = itemStack.get(ModDataComponents.ORIGIN_ITEM_DATA.get());
        if (originData == null) return null;
        return originData.toItemStack();
    }

    /**
     * 保存物品数据到目标物品
     */
    public static void copyItemStackData(ItemStack source, ItemStack target) {
        if (source.isEmpty()) return;

        ItemStack singleSource = source.copy();
        singleSource.setCount(1);

        ItemData data = ItemData.fromItemStack(singleSource);
        target.set(ModDataComponents.ORIGIN_ITEM_DATA.get(), data);
    }

    public static boolean hasItemInInventory(Item item, Player player) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasItemInInventory(Item item, Player player, int count) {
        Inventory inventory = player.getInventory();
        int stacks = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                stacks++;
            }
        }
        return stacks == count;
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
        if (!ParallelWorldsApi.isFormUnlocked(player, riderId, formId)) {
            if (player instanceof ServerPlayer serverPlayer) {
                ParallelWorldsApi.unlockForm(serverPlayer, riderId, formId);
            }
        }
    }
}