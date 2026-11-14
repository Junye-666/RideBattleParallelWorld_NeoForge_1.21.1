package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ItemFormRegistry;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
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
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.*;
import static com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig.*;
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
        ResourceLocation formId = ItemFormRegistry.getFormForItem(stack.getItem());
        if (formId == null) return;

        if (slotId.equals(ARCLE_CORE)) {
            AbilitiesUtils.unlockFormIfLocked(player, RiderIds.KUUGA_ID, formId);
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

        WeaponTransfer transfer = Tables.SWITCH_WEAPON_TRANSFERS.get(
                new FormTransition(oldFormId, newFormId));
        if (transfer != null) {
            ItemStack source = searchForItemFromPlayer(transfer.fromWeapon().get(), player);
            if (source != null) {
                transferData(source, transfer.toWeapon().toStack(), player);
            }
        } else {
            removeKuugaWeaponForForm(player, oldFormId);
        }
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.@NotNull Post event) {
        removeKuugaWeaponForForm(event.getPlayer(), event.getFormId());
    }

    private static void removeKuugaWeaponForForm(Player player, ResourceLocation formId) {
        var weapon = Tables.KUUGA_WEAPON_BY_FORM.get(formId);
        if (weapon != null) removeItemFromPlayer(weapon.get(), player);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack originalItem = event.getItemStack();
        if (!event.getSide().isServer()) return;
        if (!RideBattleAPI.isSpecificRider(player, RiderIds.KUUGA_ID)) return;

        for (WeaponConvert convert : Tables.KUUGA_WEAPON_CONVERTS) {
            if (!RideBattleAPI.isSpecificForm(player, convert.formId())) continue;

            boolean tagMatch = convert.sourceTags().stream()
                    .anyMatch(tag -> isValidItem(originalItem, tag));
            if (!tagMatch) continue;

            Item weapon = convert.weapon().get();
            if (AbilitiesUtils.countItemInInventory(weapon, player) >= convert.maxCount()) continue;

            convertItemTo(player, originalItem, weapon);
            return;
        }
    }

    // ========== 内部类型 ==========

    private record FormTransition(ResourceLocation fromForm, ResourceLocation toForm) {
    }

    private record WeaponTransfer(
            DeferredItem<? extends Item> fromWeapon,
            DeferredItem<? extends Item> toWeapon
    ) {
    }

    private record WeaponConvert(
            ResourceLocation formId,
            DeferredItem<? extends Item> weapon,
            List<TagKey<Item>> sourceTags,
            int maxCount
    ) {
        WeaponConvert(ResourceLocation formId, DeferredItem<? extends Item> weapon, TagKey<Item> tag) {
            this(formId, weapon, List.of(tag), 1);
        }

        WeaponConvert(ResourceLocation formId, DeferredItem<? extends Item> weapon, List<TagKey<Item>> tags) {
            this(formId, weapon, tags, 1);
        }
    }

    /**
     * 首次访问发生在 {@code FMLCommonSetupEvent} 之后（玩家变身 / 右键物品时）
     * </p>
     * 那时 {@code KuugaConfig.init()} 已完成，registry 已绑定。
     */
    private static final class Tables {
        static final Map<FormTransition, WeaponTransfer> SWITCH_WEAPON_TRANSFERS = Map.of(
                new FormTransition(DRAGON_ID, RISING_DRAGON_ID),
                new WeaponTransfer(DRAGON_ROD, RISING_DRAGON_ROD),
                new FormTransition(PEGASUS_ID, RISING_PEGASUS_ID),
                new WeaponTransfer(PEGASUS_BOWGUN, RISING_PEGASUS_BOWGUN),
                new FormTransition(TITAN_ID, RISING_TITAN_ID),
                new WeaponTransfer(TITAN_SWORD, RISING_TITAN_SWORD)
        );

        static final Map<ResourceLocation, DeferredItem<? extends Item>> KUUGA_WEAPON_BY_FORM = Map.of(
                DRAGON_ID, DRAGON_ROD,
                PEGASUS_ID, PEGASUS_BOWGUN,
                TITAN_ID, TITAN_SWORD,
                RISING_DRAGON_ID, RISING_DRAGON_ROD,
                RISING_PEGASUS_ID, RISING_PEGASUS_BOWGUN,
                RISING_TITAN_ID, RISING_TITAN_SWORD
        );

        static final List<WeaponConvert> KUUGA_WEAPON_CONVERTS = List.of(
                new WeaponConvert(DRAGON_ID,          DRAGON_ROD,            Tags.Items.RODS),
                new WeaponConvert(PEGASUS_ID,         PEGASUS_BOWGUN,        List.of(Tags.Items.TOOLS_BOW, Tags.Items.TOOLS_CROSSBOW)),
                new WeaponConvert(TITAN_ID,           TITAN_SWORD,           Tags.Items.MELEE_WEAPON_TOOLS),
                new WeaponConvert(RISING_DRAGON_ID,   RISING_DRAGON_ROD,     Tags.Items.RODS),
                new WeaponConvert(RISING_PEGASUS_ID,  RISING_PEGASUS_BOWGUN, List.of(Tags.Items.TOOLS_BOW, Tags.Items.TOOLS_CROSSBOW)),
                new WeaponConvert(RISING_TITAN_ID,    RISING_TITAN_SWORD,    List.of(Tags.Items.MELEE_WEAPON_TOOLS), 2)
        );

        private Tables() {
        }
    }
}