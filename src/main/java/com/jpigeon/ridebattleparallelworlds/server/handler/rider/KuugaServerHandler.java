package com.jpigeon.ridebattleparallelworlds.server.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ItemFormUtils;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.armor.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.server.util.AbilitiesUtils;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import com.jpigeon.ridebattleparallelworlds.server.util.RiderUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.*;
import static com.jpigeon.ridebattleparallelworlds.server.util.AbilitiesUtils.*;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public final class KuugaServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.KUUGA_ID;
    }

    @Override
    public void onInsert(ItemInsertionEvent.@NotNull Post event) {
        ResourceLocation slotId = event.getSlotId();
        Player player = event.getPlayer();
        ItemStack stack = event.getStack();
        ResourceLocation formId = ItemFormUtils.RIDER_ITEM_FORM_MAP.get(stack.getItem());
        if (formId == null) return;
        ResourceLocation riderId;

        if (slotId.equals(KuugaConfig.ARCLE_CORE)) {
            riderId = RiderIds.KUUGA_ID;
            AbilitiesUtils.unlockFormIfLocked(player, riderId, formId);
        } else if (slotId.equals(AgitoConfig.ALTER_RING_CORE)) {
            riderId = RiderIds.AGITO_ID;
            AbilitiesUtils.unlockFormIfLocked(player, riderId, formId);
        }
    }

    @Override
    public void onHenshinPre(HenshinEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        RideBattleParallelWorlds.LOGGER.debug("KUUGA");
        if (!(legs.getItem() instanceof ArcleItem)) return;

        if (player.isCrouching()) {
            RideBattleAPI.completeHenshin(player);
            return;
        }

        PWSkillUtils.addEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 55, 4);
        PWSkillUtils.addResistance(player, 80);
        RiderUtils.playSound(player, ModSounds.ARCLE_APPEAR.get());

        FormConfig form = RideBattleAPI.getFormConfig(player, event.getFormId());
        scheduleTicks(10, () -> RiderUtils.playHenshinSound(player, form));
        ModSounds.getSoundLength(form).ifPresent(len -> RideBattleAPI.completeIn(len, player));
    }

    @Override
    public void onHenshinPost(HenshinEvent.@NotNull Post event) {
        Player player = event.getPlayer();

        PWSkillUtils.addSaturation(player, 40);
        PWSkillUtils.addRegeneration(player, 20);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getNewFormId();

        if (player.isCrouching()) {
            RideBattleAPI.completeHenshin(player);
            return;
        }

        FormConfig form = RideBattleAPI.getFormConfig(player, formId);
        RiderUtils.playHenshinSound(player, form);
        ModSounds.getSoundLength(form).ifPresent(len -> RideBattleAPI.completeIn(len, player));
    }

    @Override
    public void onSwitchPost(FormSwitchEvent.@NotNull Post event) {
        Player player = event.getPlayer();
        ResourceLocation oldFormId = event.getOldFormId();
        ResourceLocation newFormId = event.getNewFormId();
        if (oldFormId.equals(KuugaConfig.DRAGON_ID) && newFormId.equals(KuugaConfig.RISING_DRAGON_ID)) {
            ItemStack dragonRod = searchForItemFromPlayer(DRAGON_ROD.get(), player);
            if (dragonRod != null) {
                ItemStack rising = RISING_DRAGON_ROD.toStack();
                transferData(dragonRod, rising, player);
            }
        } else if (oldFormId.equals(KuugaConfig.PEGASUS_ID) && newFormId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
            ItemStack pegasusBowGun = searchForItemFromPlayer(PEGASUS_BOWGUN.get(), player);
            if (pegasusBowGun != null) {
                ItemStack rising = RISING_PEGASUS_BOWGUN.toStack();
                transferData(pegasusBowGun, rising, player);
            }
        } else if (oldFormId.equals(KuugaConfig.TITAN_ID) && newFormId.equals(KuugaConfig.RISING_TITAN_ID)) {
            ItemStack titanSword = searchForItemFromPlayer(TITAN_SWORD.get(), player);
            if (titanSword != null) {
                ItemStack rising = RISING_TITAN_SWORD.toStack();
                transferData(titanSword, rising, player);
            }
        } else {
            if (oldFormId.equals(KuugaConfig.DRAGON_ID)) removeItemFromPlayer(DRAGON_ROD.get(), player);
            else if (oldFormId.equals(KuugaConfig.PEGASUS_ID))
                removeItemFromPlayer(PEGASUS_BOWGUN.get(), player);
            else if (oldFormId.equals(KuugaConfig.TITAN_ID)) removeItemFromPlayer(TITAN_SWORD.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_DRAGON_ID))
                removeItemFromPlayer(RISING_DRAGON_ROD.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_PEGASUS_ID))
                removeItemFromPlayer(RISING_PEGASUS_BOWGUN.get(), player);
            else if (oldFormId.equals(KuugaConfig.RISING_TITAN_ID))
                removeItemFromPlayer(RISING_TITAN_SWORD.get(), player);
        }
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.@NotNull Post event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();
        if (formId.equals(KuugaConfig.DRAGON_ID)) removeItemFromPlayer(DRAGON_ROD.get(), player);
        else if (formId.equals(KuugaConfig.PEGASUS_ID)) removeItemFromPlayer(PEGASUS_BOWGUN.get(), player);
        else if (formId.equals(KuugaConfig.TITAN_ID)) removeItemFromPlayer(TITAN_SWORD.get(), player);
        else if (formId.equals(KuugaConfig.RISING_DRAGON_ID))
            removeItemFromPlayer(RISING_DRAGON_ROD.get(), player);
        else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID))
            removeItemFromPlayer(RISING_PEGASUS_BOWGUN.get(), player);
        else if (formId.equals(KuugaConfig.RISING_TITAN_ID))
            removeItemFromPlayer(RISING_TITAN_SWORD.get(), player);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack originalItem = event.getItemStack();

        if (RideBattleAPI.isSpecificRider(player, RiderIds.KUUGA_ID)) {
            if (RideBattleAPI.isSpecificForm(player, KuugaConfig.DRAGON_ID) && !hasItemInInventory(DRAGON_ROD.get(), player) && isValidItem(originalItem, Tags.Items.RODS)) {
                convertItemTo(player, originalItem, DRAGON_ROD.get());
            } else if (RideBattleAPI.isSpecificForm(player, KuugaConfig.PEGASUS_ID) && !hasItemInInventory(PEGASUS_BOWGUN.get(), player) && (isValidItem(originalItem, Tags.Items.TOOLS_BOW) || isValidItem(originalItem, Tags.Items.TOOLS_CROSSBOW))) {
                convertItemTo(player, originalItem, PEGASUS_BOWGUN.get());
            } else if (RideBattleAPI.isSpecificForm(player, KuugaConfig.TITAN_ID) && !hasItemInInventory(TITAN_SWORD.get(), player) && isValidItem(originalItem, Tags.Items.MELEE_WEAPON_TOOLS)) {
                convertItemTo(player, originalItem, TITAN_SWORD.get());
            } else if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_DRAGON_ID) && !hasItemInInventory(RISING_DRAGON_ROD.get(), player) && isValidItem(originalItem, Tags.Items.RODS)) {
                convertItemTo(player, originalItem, RISING_DRAGON_ROD.get());
            } else if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_PEGASUS_ID) && !hasItemInInventory(RISING_PEGASUS_BOWGUN.get(), player) && (isValidItem(originalItem, Tags.Items.TOOLS_BOW) || isValidItem(originalItem, Tags.Items.TOOLS_CROSSBOW))) {
                convertItemTo(player, originalItem, RISING_PEGASUS_BOWGUN.get());
            } else if (RideBattleAPI.isSpecificForm(player, KuugaConfig.RISING_TITAN_ID) && !AbilitiesUtils.hasItemInInventory(RISING_TITAN_SWORD.get(), player, 2) && isValidItem(originalItem, Tags.Items.MELEE_WEAPON_TOOLS)) {
                convertItemTo(player, originalItem, RISING_TITAN_SWORD.get());
            }
        }
    }

    private static boolean isValidItem(ItemStack itemStack, TagKey<Item> tagKey) {
        return AbilitiesUtils.isValidItem(itemStack, tagKey);
    }

    private static void convertItemTo(Player player, ItemStack originStack, Item targetItem) {
        AbilitiesUtils.convertItemTo(player, originStack, targetItem);
    }

    private static boolean hasItemInInventory(Item item, Player player) {
        return AbilitiesUtils.hasItemInInventory(item, player);
    }

    private static void scheduleTicks(int ticks, Runnable callback) {
        RideBattleAPI.scheduleTicks(ticks, callback);
    }
}