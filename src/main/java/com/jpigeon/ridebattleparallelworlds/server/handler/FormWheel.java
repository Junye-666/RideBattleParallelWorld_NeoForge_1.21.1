package com.jpigeon.ridebattleparallelworlds.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.server.event.ReturnItemsEvent;
import com.jpigeon.ridebattlelib.server.event.SlotExtractionEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.api.ParallelWorldsApi;
import com.jpigeon.ridebattleparallelworlds.common.registry.ItemFormRegistry;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.armor.ArcleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormWheel {
    private static final Map<UUID, ResourceLocation> PENDING_FORMS = new HashMap<>();

    private static boolean isWheelRider(ResourceLocation riderId) {
        return RiderIds.KUUGA_ID.equals(riderId) || RiderIds.AGITO_ID.equals(riderId);
    }

    /**
     * 在返还物品之前记录当前形态。
     * <p>
     * 初生形态没有核心物品，因此不能依赖 SlotExtractionEvent
     * 获取当前形态。
     */
    @SubscribeEvent
    public static void onReturnItemsPre(ReturnItemsEvent.Pre event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UUID uuid = player.getUUID();
        RiderConfig config = event.getConfig();

        if (!player.isCrouching() || !isWheelRider(config.getRiderId())) {
            PENDING_FORMS.remove(uuid);
            return;
        }

        FormConfig currentForm = config.getActiveFormConfig(player);

        if (currentForm != null) {
            PENDING_FORMS.put(uuid, currentForm.getFormId());
        } else {
            PENDING_FORMS.remove(uuid);
        }
    }

    @SubscribeEvent
    public static void onExtract(SlotExtractionEvent.Pre event) {
        ResourceLocation slotId = event.getSlotId();

        if (!slotId.equals(KuugaConfig.ARCLE_CORE)
                && !slotId.equals(AgitoConfig.ALTER_RING_CORE)) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) return;

        // 必须在 setAir() 之前读取核心槽当前物品。
        ItemStack currentStack = RideBattleAPI.getItemForSlot(player, slotId);

        if (!currentStack.isEmpty()) {
            ResourceLocation currentForm =
                    ItemFormRegistry.getFormForItem(currentStack.getItem());

            // 不覆盖 Pre 阶段记录的有效形态。
            if (currentForm != null) {
                PENDING_FORMS.put(player.getUUID(), currentForm);
            }
        }

        // 轮盘切换时不返还核心物品给玩家。
        event.setAir();
    }

    @SubscribeEvent
    public static void onReturnItem(ReturnItemsEvent.Post event) {
        Player player = event.getPlayer();
        if (player == null) return;

        // 无论后续是否执行轮盘，都先清理缓存。
        ResourceLocation currentForm =
                PENDING_FORMS.remove(player.getUUID());

        if (!player.isCrouching()) return;

        RiderConfig config = event.getConfig();

        if (!isWheelRider(config.getRiderId())) {
            return;
        }

        // 兜底：初生形态的核心槽为空，
        // 返还完成后重新匹配仍能得到 Growing Form。
        if (currentForm == null) {
            FormConfig matchedForm = config.getActiveFormConfig(player);

            if (matchedForm != null) {
                currentForm = matchedForm.getFormId();
            }
        }

        handleRotateInternal(player, currentForm, config);
    }

    private static void handleRotateInternal(
            Player player,
            @Nullable ResourceLocation currentForm,
            RiderConfig config
    ) {
        ResourceLocation riderId = config.getRiderId();

        boolean isKuuga = RiderIds.KUUGA_ID.equals(riderId);

        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);

        if (isKuuga && !(legs.getItem() instanceof ArcleItem)) {
            return;
        }

        if (!isKuuga && !(legs.getItem() instanceof AlterRingItem)) {
            return;
        }

        ResourceLocation coreSlotId = isKuuga
                ? KuugaConfig.ARCLE_CORE
                : AgitoConfig.ALTER_RING_CORE;


        // 使用 RiderConfig 中的注册顺序，
        List<ResourceLocation> unlockedForms = config.getForms()
                .keySet()
                .stream()
                .filter(formId ->
                        ParallelWorldsApi.isFormUnlocked(
                                player,
                                riderId,
                                formId
                        )
                )
                .toList();

        if (unlockedForms.isEmpty()) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(
                        Component.literal("没有可用的解锁形态")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            return;
        }

        int currentIdx = currentForm == null
                ? -1
                : unlockedForms.indexOf(currentForm);

        if (currentForm != null && currentIdx == -1) {
            if (Config.debug()) {
                RideBattleParallelWorlds.LOGGER.warn(
                        "FormWheel: 当前形态不在解锁列表中。player={}, rider={}, current={}, unlocked={}",
                        player.getName().getString(),
                        riderId,
                        currentForm,
                        unlockedForms
                );
            }
        }

        int nextIndex = (currentIdx + 1) % unlockedForms.size();

        ResourceLocation newId = unlockedForms.get(nextIndex);

        // 更新驱动器槽位。
        boolean success = setDriverSlotFor(player, coreSlotId, newId);

        if (!success) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: 形态切换失败。player={}, rider={}, oldForm={}, newForm={}, slot={}",
                    player.getName().getString(),
                    riderId,
                    currentForm,
                    newId,
                    coreSlotId
            );


            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(
                        Component.literal("形态切换失败，请检查服务端日志")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }

            return;
        }

        // 只有插入成功后才显示新形态。
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(
                    getDisplayName(newId),
                    true
            );
        }
    }

    /**
     * 将形态映射为核心槽物品。
     * <p>
     * Kuuga Growing Form 特殊处理：
     * 它没有 Element，空核心槽本身就是正确状态。
     */
    private static boolean setDriverSlotFor(
            Player player,
            ResourceLocation coreSlotId,
            ResourceLocation formId
    ) {
        if (player == null || formId == null) {
            return false;
        }

        RiderConfig config = RideBattleAPI.getActiveRiderConfig(player);
        if (config == null) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: 找不到当前驱动器配置。player={}",
                    player.getName().getString()
            );
            return false;
        }

        // 直接从形态配置读取核心物品，不依赖 ItemFormRegistry。
        FormConfig formConfig = config.getForms().get(formId);

        if (formConfig == null) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: 找不到形态配置。rider={}, form={}",
                    config.getRiderId(),
                    formId
            );
            return false;
        }

        Item item = formConfig.getRequiredItems().get(coreSlotId);

        if (item == null) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: 形态没有声明目标核心槽物品。form={}, slot={}",
                    formId,
                    coreSlotId
            );
            return false;
        }

        // Growing Form 的 requiredItem 是 Items.AIR：
        // 空核心槽就是正确的形态配置。
        if (item == Items.AIR) {
            ItemStack currentStack =
                    RideBattleAPI.getItemForSlot(player, coreSlotId);

            if (!currentStack.isEmpty()) {
                RideBattleAPI.extractItemFromSlot(player, coreSlotId);
            }

            return RideBattleAPI.getItemForSlot(
                    player,
                    coreSlotId
            ).isEmpty();
        }

        // 防御性检查：目标物品必须允许放入这个槽位。
        var slot = config.getSlotDefinition(coreSlotId);

        if (slot == null || !slot.allowedItems().contains(item)) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: 目标核心物品不被槽位允许。rider={}, form={}, slot={}, item={}",
                    config.getRiderId(),
                    formId,
                    coreSlotId,
                    BuiltInRegistries.ITEM.getKey(item)
            );
            return false;
        }

        if (player.getCooldowns().isOnCooldown(item)) {
            if (Config.debug())
                RideBattleParallelWorlds.LOGGER.error("FormWheel: 目标核心物品处于冷却中。item={}", BuiltInRegistries.ITEM.getKey(item));

            return false;
        }

        boolean inserted = RideBattleAPI.insertItemToSlot(
                player,
                coreSlotId,
                item.getDefaultInstance()
        );

        if (!inserted) {
            RideBattleParallelWorlds.LOGGER.error(
                    "FormWheel: DriverSystem 拒绝插入。form={}, item={}, slot={}",
                    formId,
                    BuiltInRegistries.ITEM.getKey(item),
                    coreSlotId
            );
        }

        return inserted;
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
}

