package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.core.component.ItemData;
import com.jpigeon.ridebattleparallelworlds.core.component.ModDataComponents;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class AbilitiesHandler {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack originalItem = event.getItemStack();
        if (RiderManager.isSpecificRider(player, RiderIds.KUUGA_ID)) {
            if (RiderManager.isSpecificForm(player, KuugaConfig.DRAGON_ID) && !hasItemInInventory(ModItems.DRAGON_ROD.get(), player) && isValidItem(originalItem, Tags.Items.RODS)) {
                convertItemTo(player, originalItem, ModItems.DRAGON_ROD.get());
            } else if (RiderManager.isSpecificForm(player, KuugaConfig.PEGASUS_ID) && !hasItemInInventory(ModItems.PEGASUS_BOWGUN.get(), player) && (isValidItem(originalItem, Tags.Items.TOOLS_BOW) || isValidItem(originalItem, Tags.Items.TOOLS_CROSSBOW))) {
                convertItemTo(player, originalItem, ModItems.PEGASUS_BOWGUN.get());
            } else if (RiderManager.isSpecificForm(player, KuugaConfig.TITAN_ID) && !hasItemInInventory(ModItems.TITAN_SWORD.get(), player) && isValidItem(originalItem, Tags.Items.MELEE_WEAPON_TOOLS)) {
                convertItemTo(player, originalItem, ModItems.TITAN_SWORD.get());
            } else if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_DRAGON_ID) && !hasItemInInventory(ModItems.RISING_DRAGON_ROD.get(), player) && isValidItem(originalItem, Tags.Items.RODS)) {
                convertItemTo(player, originalItem, ModItems.RISING_DRAGON_ROD.get());
            } else if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_PEGASUS_ID) && !hasItemInInventory(ModItems.RISING_PEGASUS_BOWGUN.get(), player) && (isValidItem(originalItem, Tags.Items.TOOLS_BOW) || isValidItem(originalItem, Tags.Items.TOOLS_CROSSBOW))) {
                convertItemTo(player, originalItem, ModItems.RISING_PEGASUS_BOWGUN.get());
            } else if (RiderManager.isSpecificForm(player, KuugaConfig.RISING_TITAN_ID) && !hasItemInInventory(ModItems.RISING_TITAN_SWORD.get(), player, 2) && isValidItem(originalItem, Tags.Items.MELEE_WEAPON_TOOLS)) {
                convertItemTo(player, originalItem, ModItems.RISING_TITAN_SWORD.get());
            }
        }
    }

    @SubscribeEvent
    public static void onUnhenshin(UnhenshinEvent.Post event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();
        if (formId.equals(KuugaConfig.DRAGON_ID)) removeItemFromPlayer(ModItems.DRAGON_ROD.get(), player);
        else if (formId.equals(KuugaConfig.PEGASUS_ID)) removeItemFromPlayer(ModItems.PEGASUS_BOWGUN.get(), player);
        else if (formId.equals(KuugaConfig.TITAN_ID)) removeItemFromPlayer(ModItems.TITAN_SWORD.get(), player);
        else if (formId.equals(KuugaConfig.RISING_DRAGON_ID))
            removeItemFromPlayer(ModItems.RISING_DRAGON_ROD.get(), player);
        else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID))
            removeItemFromPlayer(ModItems.RISING_PEGASUS_BOWGUN.get(), player);
        else if (formId.equals(KuugaConfig.RISING_TITAN_ID))
            removeItemFromPlayer(ModItems.RISING_TITAN_SWORD.get(), player);
    }

    @SubscribeEvent
    public static void onHit(AttackEntityEvent event) {
        Player player = event.getEntity();
        LivingEntity target = event.getTarget().getControllingPassenger();
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHand.getItem() instanceof FlameSaberItem flameSaber && !flameSaber.getCurrentAnimState().equals("idle")) {
            flameSaber.setClose();
        }
    }

    @SubscribeEvent
    public static void postSwitch(FormSwitchEvent.Post event) {
        Player player = event.getPlayer();
        ResourceLocation oldFormId = event.getOldFormId();
        ResourceLocation newFormId = event.getNewFormId();
        if (oldFormId.equals(KuugaConfig.DRAGON_ID) && newFormId.equals(KuugaConfig.RISING_DRAGON_ID)) {
            ItemStack dragonRod = searchForItemFromPlayer(ModItems.DRAGON_ROD.get(), player);
            if (dragonRod != null) {
                ItemStack rising = ModItems.RISING_DRAGON_ROD.toStack();
                transferData(dragonRod, rising, player);
            }
        } else if (oldFormId.equals(KuugaConfig.PEGASUS_ID) && newFormId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
            ItemStack pegasusBowGun = searchForItemFromPlayer(ModItems.PEGASUS_BOWGUN.get(), player);
            if (pegasusBowGun != null) {
                ItemStack rising = ModItems.RISING_PEGASUS_BOWGUN.toStack();
                transferData(pegasusBowGun, rising, player);
            }
        } else if (oldFormId.equals(KuugaConfig.TITAN_ID) && newFormId.equals(KuugaConfig.RISING_TITAN_ID)) {
            ItemStack titanSword = searchForItemFromPlayer(ModItems.TITAN_SWORD.get(), player);
            if (titanSword != null) {
                ItemStack rising = ModItems.RISING_TITAN_SWORD.toStack();
                transferData(titanSword, rising, player);
            }
        } else {
            if (oldFormId.equals(KuugaConfig.DRAGON_ID)) removeItemFromPlayer(ModItems.DRAGON_ROD.get(), player);
            else if (oldFormId.equals(KuugaConfig.PEGASUS_ID)) removeItemFromPlayer(ModItems.PEGASUS_BOWGUN.get(), player);
            else if (oldFormId.equals(KuugaConfig.TITAN_ID)) removeItemFromPlayer(ModItems.TITAN_SWORD.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_DRAGON_ID)) removeItemFromPlayer(ModItems.RISING_DRAGON_ROD.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_PEGASUS_ID)) removeItemFromPlayer(ModItems.RISING_PEGASUS_BOWGUN.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_TITAN_ID)) removeItemFromPlayer(ModItems.RISING_TITAN_SWORD.get(), player);
        }
    }

    private static void convertItemTo(Player player, ItemStack originStack, Item targetItem) {
        ItemStack targetStack = targetItem.getDefaultInstance();
        copyItemStackData(originStack, targetStack);
        originStack.shrink(1);
        if (!player.getInventory().add(targetStack)) {
            player.drop(targetStack, false);
        }
        player.getCooldowns().addCooldown(targetItem, 10);
    }

    private static boolean isValidItem(ItemStack itemStack, TagKey<Item> tagKey) {
        return itemStack.is(tagKey);
    }

    private static void removeItemFromPlayer(Item toRemove, Player player) {
        Inventory inventory = player.getInventory();
        boolean found = false;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && stack.getItem() == toRemove) {
                found = true;

                ItemStack restoredStack = toRestoredItem(stack);

                // 移除龙杖
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

    private static ItemStack searchForItemFromPlayer(Item item, Player player) {
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

    private static boolean hasItemInInventory(Item item, Player player) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasItemInInventory(Item item, Player player, int count) {
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

    private static void transferData(ItemStack originItem, ItemStack targetItem, Player player) {
        if (originItem.isEmpty() || targetItem.isEmpty()) {
            return;
        }
        ItemData originData = originItem.get(ModDataComponents.ORIGIN_ITEM_DATA.get());
        targetItem.set(ModDataComponents.ORIGIN_ITEM_DATA.get(), originData);
        replaceItemStackWith(originItem, targetItem, player);
    }
}