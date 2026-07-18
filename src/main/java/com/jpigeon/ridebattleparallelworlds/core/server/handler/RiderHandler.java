package com.jpigeon.ridebattleparallelworlds.core.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.common.event.*;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWClientItemEventPacket;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWClientStateEventPacket;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.entity.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.entity.RyukiHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

/**
 * 管业务逻辑
 */
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderHandler {
    @SubscribeEvent
    public static void onHenshin(HenshinEvent.Pre event) {
        RideBattleParallelWorlds.LOGGER.debug("HENSHIN");
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        ResourceLocation riderId = event.getRiderId();

        sendStateEventToClient(player, "henshin", riderId, formId);
        // 按riderId处理
        if (riderId.equals(RiderIds.KUUGA_ID)) {
            henshinKuuga(player, legs, formId);
        } else if (riderId.equals(RiderIds.AGITO_ID)) {
            completeAgito(player, legs, formId);
        } else if (riderId.equals(RiderIds.MIRROR_SYSTEM_ID)) {
            henshinMirror(player, formId);
        } else if (riderId.equals(RiderIds.DECADE_ID)) {
            henshinDecade(player, formId);
        }
    }

    @SubscribeEvent
    public static void postUnhenshin(UnhenshinEvent.Post event) {
        Player player = event.getPlayer();

        sendStateEventToClient(player, "unhenshin", event.getRiderId(), event.getFormId());

        if (event.getRiderId().equals(RiderIds.AGITO_ID)) {
            removeAgitoWeapon(player);
        }
        RiderSkills.SKILL_TAGS_MAP.values().stream().filter(tag -> tag.startsWith("skill_"))
                .forEach(skillTag -> {
                    if (player.getTags().contains(skillTag)) {
                        player.removeTag(skillTag);
                    }
                });
    }

    @SubscribeEvent
    public static void onSwitch(FormSwitchEvent.Pre event) {
        RideBattleParallelWorlds.LOGGER.debug("SWITCH");
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation newFormId = event.getNewFormId();

        RiderConfig config = RiderConfig.findActiveDriverConfig(player);
        sendStateEventToClient(player, "switch", config.getRiderId(), newFormId);

        // 按config处理
        if (config == KuugaConfig.KUUGA) {
            switchKuuga(player, newFormId);
        } else if (config == AgitoConfig.AGITO) {
            removeAgitoWeapon(player);
            completeAgito(event.getPlayer(), legs, newFormId);
        } else if (config == DecadeConfig.DECADE) {
            henshinDecade(event.getPlayer(), newFormId);
        }
    }

    @SubscribeEvent
    public static void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack stack = event.getStack();
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            playSound(player, ModSounds.DECADE_INSERT.get());
            scheduleTicks(5, decaDriver::triggerClose);
            if (isValidItem(stack, ModTags.Items.KAMEN_RIDE_CARDS)) {
                scheduleTicks(5, () -> playSound(player, ModSounds.KAMEN_RIDE.get()));
            } else if (isValidItem(stack, ModTags.Items.FORM_RIDE_CARDS)) {
                scheduleTicks(5, () -> playSound(player, ModSounds.FORM_RIDE.get()));
            }
        } else if (legs.getItem() instanceof AlterRingItem) {
            prepareAgito(player);
        }

        sendItemEventToClient(player, "insert", event.getConfig().getRiderId(), stack);
    }

    @SubscribeEvent
    public static void onExtract(SlotExtractionEvent.Post event) {
        sendItemEventToClient(event.getPlayer(), "extract", event.getConfig().getRiderId(), event.getExtractedStack());
    }

    // 变身辅助
    private static void henshinKuuga(Player player, ItemStack legs, ResourceLocation formId) {
        if (!(legs.getItem() instanceof ArcleItem)) return;
        if (player.isCrouching()) {
            RideBattleAPI.completeHenshin(player);
            return;
        }
        SkillHandler.addEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 55, 4);
        SkillHandler.addResistance(player, 120);
        playSound(player, ModSounds.ARCLE_APPEAR.get());

        FormConfig form = RideBattleAPI.getFormConfig(player, formId);

        scheduleTicks(10, () -> playHenshinSound(player, form));
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> completeIn(integer, player));
    }

    private static void switchKuuga(Player player, ResourceLocation formId) {
        if (player.isCrouching()) {
            RideBattleAPI.completeHenshin(player);
            return;
        }

        FormConfig form = RideBattleAPI.getFormConfig(player, formId);
        playHenshinSound(player, form);
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> completeIn(integer, player));
    }

    private static void prepareAgito(Player player) {
        playSound(player, ModSounds.AGITO_PREPARE.get());
        if (!isTransformed(player)) playSound(player, ModSounds.AGITO_STEADY.get());
    }

    private static void completeAgito(Player player, ItemStack legs, ResourceLocation formId) {
        // TODO : 燃烧/闪耀相关音效
        playSound(player, ModSounds.AGITO_FINISH.get());
        completeIn(10, player);
    }

    private static void removeAgitoWeapon(Player player) {
        List<Item> toRemove = List.of(ModItems.FLAME_SABER.get(), ModItems.STORM_HALBERD.get(), ModItems.SHINING_CALIBUR.get());
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (toRemove.contains(stack.getItem())) {
                int removeAmount = stack.getCount();
                stack.shrink(removeAmount);
            }
        }
    }

    private static void henshinMirror(Player player, ResourceLocation formId) {
        // TODO : 镜系统完善
        FormConfig form = RideBattleAPI.getFormConfig(player, formId);
        // TODO playHenshinSound(player, form);
        Level level = player.level();

        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) {
            RyukiHenshinEffect effect = new RyukiHenshinEffect(
                    ModEntities.RYUKI_HENSHIN_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());
            effect.setOwner(player);

            scheduleTicks(10, () -> level.addFreshEntity(effect));
            completeIn(22, player);
        }
    }

    private static void henshinDecade(Player player, ResourceLocation formId) {
        FormConfig form = RideBattleAPI.getFormConfig(player, formId);
        scheduleTicks(10, () -> playHenshinSound(player, form));
        if (formId.equals(DecadeConfig.DECADE_BASE_ID)) {
            Level level = player.level();

            DecadeHenshinEffect effect = new DecadeHenshinEffect(
                    ModEntities.DECADE_SPECIAL_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());

            effect.setOwner(player);

            scheduleTicks(20, () -> level.addFreshEntity(effect));
        }
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> completeIn(integer, player));
    }

    private static void playHenshinSound(Player player, FormConfig form) {
        Optional<SoundEvent> sound = ModSounds.getHenshinSound(form);
        if (sound.isEmpty()) return;
        playSound(player, sound.get());
    }

    public static void playSound(Player player, SoundEvent soundEvent) {
        RideBattleAPI.playPublicSound(player, soundEvent, ((float) Config.RIDER_SOUNDS_VOLUME.get() / 100));
    }

    private static boolean isValidItem(ItemStack itemStack, TagKey<Item> tagKey) {
        return itemStack.is(tagKey);
    }

    private static void sendStateEventToClient(Player player, String eventType, ResourceLocation riderId, ResourceLocation formId) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new PWClientStateEventPacket(eventType, riderId, formId));
    }

    private static void sendItemEventToClient(Player player, String eventType, ResourceLocation riderId, ItemStack stack) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new PWClientItemEventPacket(eventType, stack));
    }

    private static void scheduleTicks(int ticks, Runnable runnable) {
        RideBattleAPI.scheduleTicks(ticks, runnable);
    }

    private static void completeIn(int ticks, Player player) {
        RideBattleAPI.completeIn(ticks, player);
    }

    private static boolean isTransformed(Player player) {
        return RideBattleAPI.isTransformed(player);
    }
}
