package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.*;
import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.network.handler.PacketHandler;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.extra.shocker.ShockerConfig;
import com.jpigeon.ridebattleparallelworlds.core.handler.util.ModTags;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.network.packet.PWAnimationPacket;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.Optional;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderHandler {
    @SubscribeEvent
    public static void onHenshin(HenshinEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        ResourceLocation riderId = event.getRiderId();
        // 处理空我
        if (riderId.equals(RiderIds.KUUGA_ID)) {
            handleKuuga(player, legs, formId);
        } else if (riderId.equals(RiderIds.AGITO_ID)) {
            completeAgito(player, legs, formId);
        } else if (riderId.equals(RiderIds.DECADE_ID)) {
            handleDecade(player, formId);
        }
    }

    @SubscribeEvent
    public static void onUnhenshin(UnhenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        switch (legs.getItem()) {
            case ArcleItem arcle -> arcle.shrinkInBody();
            case AlterRingItem alterRing -> alterRing.shrinkInBody();
            case DecaDriverItem decaDriver -> decaDriver.triggerOpen();
            default -> {
            }
        }
        Player player = event.getPlayer();
        if (event.getRiderId().equals(RiderIds.AGITO_ID)) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (ItemStack.isSameItem(stack, ModItems.FLAME_SABER.get().getDefaultInstance()) || ItemStack.isSameItem(stack, ModItems.STORM_HALBERD.get().getDefaultInstance())) {
                    int removeAmount = stack.getCount();
                    stack.shrink(removeAmount);
                }
            }
        }
        RiderSkills.BUFFERED_SKILL_TAGS.values().stream().filter(tag -> tag.startsWith("skill_"))
                .forEach(skillTag -> {
                    if (player.getTags().contains(skillTag)) {
                        player.removeTag(skillTag);
                    }
                });
    }

    @SubscribeEvent
    public static void onSwitch(FormSwitchEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation newFormId = event.getNewFormId();
        RiderConfig config = RiderConfig.findActiveDriverConfig(player);
        // 处理空我
        if (config == KuugaConfig.KUUGA) {
            handleKuugaSwitch(player, legs, newFormId);
        } else if (config == AgitoConfig.AGITO) {
            completeAgito(event.getPlayer(), legs, newFormId);
        } else if (config == DecadeConfig.DECADE) {
            handleDecade(event.getPlayer(), newFormId);
        }

    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        setDriverAnim(legs, formId);
        ItemStack head = event.getPlayer().getItemBySlot(EquipmentSlot.HEAD);
        if (head.getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.setCurrentState(AgitoGroundItem.AnimState.IDLE);
        }
        if (formId.equals(ShockerConfig.COMBATMAN_ID)) {
            playAnimation(event.getPlayer(), "shocker_greeting");
        }
    }

    @SubscribeEvent
    public static void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            playSound(player, ModSounds.DECADE_INSERT.get());
            RiderManager.scheduleTicks(5, decaDriver::triggerClose);
            ItemStack stack = event.getStack();
            if (isValidItem(stack, ModTags.Items.KAMEN_RIDE_CARDS)) {
                RiderManager.scheduleTicks(5, () -> playSound(player, ModSounds.KAMEN_RIDE.get()));
            } else if (isValidItem(stack, ModTags.Items.FORM_RIDE_CARDS)) {
                RiderManager.scheduleTicks(5, () -> playSound(player, ModSounds.FORM_RIDE.get()));
            }
        } else if (legs.getItem() instanceof AlterRingItem) {
            prepareAgito(player, legs);
        }
    }

    @SubscribeEvent
    public static void onExtract(SlotExtractionEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            decaDriver.triggerOpen();
        }
    }

    public static void setDriverAnim(ItemStack legs, ResourceLocation formId) {
        if (legs.getItem() instanceof ArcleItem arcle) {
            arcle.setStateByFormId(formId);
        } else if (legs.getItem() instanceof AlterRingItem alterRing) {
            alterRing.setStateByFormId(formId);
        }
    }

    private static void playHenshinSound(Player player, FormConfig form) {
        Optional<SoundEvent> sound = ModSounds.getHenshinSound(form);
        if (sound.isEmpty()) return;
        playSound(player, sound.get());
    }

    private static void playAnimation(Player player, String animationId, int fadeDuration) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketHandler.sendToClient(serverPlayer, new PWAnimationPacket(player.getUUID(), animationId, fadeDuration));
        }
    }

    private static void playAnimation(Player player, String animationId) {
        playAnimation(player, animationId, 0);
    }

    // 变身辅助
    private static void handleKuuga(Player player, ItemStack legs, ResourceLocation formId) {
        if (player.isCrouching()) {
            RiderManager.completeHenshin(player);
            return;
        }
        SkillHandler.addEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 55, 4);
        SkillHandler.addResistance(player, 120);
        playAnimation(player, "kuuga_henshin");
        if (legs.getItem() instanceof ArcleItem arcleItem) {
            playSound(player, ModSounds.ARCLE_APPEAR.get());
            if (arcleItem.getCurrentAnimState().equals("inBody") || arcleItem.getCurrentAnimState().equals("shrink")) {
                RiderManager.scheduleTicks(5, arcleItem::triggerAppear);
            }
        }
        FormConfig form = RiderManager.getFormConfig(player, formId);

        RiderManager.scheduleTicks(36, () -> playHenshinSound(player, form));
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> RiderManager.completeIn(integer, player));
    }

    private static void handleKuugaSwitch(Player player, ItemStack legs, ResourceLocation formId) {
        if (player.isCrouching()) {
            RiderManager.completeHenshin(player);
            setDriverAnim(legs, formId);
            return;
        }
        playAnimation(player, "kuuga_switch");
        RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, formId));
        FormConfig form = RiderManager.getFormConfig(player, formId);
        playHenshinSound(player, form);
        RiderManager.completeIn(90, player);
    }


    private static void prepareAgito(Player player, ItemStack legs) {
        playAnimation(player, "agito_prepare");
        playSound(player, ModSounds.AGITO_PREPARE.get());
        if (!RiderManager.isTransformed(player))
            RiderManager.scheduleTicks(20, () -> playSound(player, ModSounds.AGITO_STEADY.get()));
        if (legs.getItem() instanceof AlterRingItem alterRing && (alterRing.getCurrentAnimState().equals("inBody") || alterRing.getCurrentAnimState().equals("shrink"))) {
            alterRing.triggerAppear();
            setDriverAnim(legs, RiderManager.getPendingForm(player));
        }
    }

    private static void completeAgito(Player player, ItemStack legs, ResourceLocation formId) {
        playAnimation(player, "agito_henshin");
        Minecraft.getInstance().getSoundManager().stop();
        playSound(player, ModSounds.AGITO_FINISH.get());
        RiderManager.completeIn(10, player);
        RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, formId));
    }

    private static void handleDecade(Player player, ResourceLocation formId) {
        FormConfig form = RiderManager.getFormConfig(player, formId);
        playAnimation(player, "decade_insert");
        RiderManager.scheduleTicks(10, () -> playHenshinSound(player, form));
        if (formId.equals(DecadeConfig.DECADE_BASE_ID)) {
            Level level = player.level();

            DecadeHenshinEffect effect = new DecadeHenshinEffect(
                    ModEntities.DECADE_SPECIAL_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());

            effect.setOwner(player);

            RiderManager.scheduleTicks(20, () -> level.addFreshEntity(effect));

        }
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> RiderManager.completeIn(integer, player));
    }

    private static boolean isValidItem(ItemStack itemStack, TagKey<Item> tagKey) {
        return itemStack.is(tagKey);
    }

    public static void playSound(Player player, SoundEvent soundEvent) {
        RiderManager.playPublicSound(player, soundEvent, ((float) Config.RIDER_SOUNDS_VOLUME.get() / 100));
    }
}
