package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.*;
import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.DecadeSpecialEffect;
import com.jpigeon.ridebattleparallelworlds.core.handler.util.ModTags;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
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

import java.util.Optional;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderHandler {
    @SubscribeEvent
    public static void onHenshin(HenshinEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        // 处理空我
        if (event.getRiderId().equals(RiderIds.KUUGA_ID)) {
            handleKuuga(player, legs, formId);
        }
        if (event.getRiderId().equals(RiderIds.DECADE_ID)) {
            handleDecade(event.getPlayer(), formId);
        }
    }

    @SubscribeEvent
    public static void onUnhenshin(UnhenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof ArcleItem arcle) {
            arcle.shrinkInBody();
        }
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            decaDriver.triggerOpen();
        }
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
        } else if (config == DecadeConfig.DECADE) {
            handleDecade(event.getPlayer(), newFormId);
        }

    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        setDriverAnim(legs, formId);
    }

    @SubscribeEvent
    public static void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            RiderManager.playPublicSound(player, ModSounds.DECADE_INSERT.get());
            RiderManager.scheduleTicks(5, decaDriver::triggerClose);
            ItemStack stack = event.getStack();
            if (isValidItem(stack, ModTags.Items.KAMEN_RIDE_CARDS)) {
                RiderManager.scheduleTicks(5, () -> RiderManager.playPublicSound(player, ModSounds.KAMEN_RIDE.get()));
            } else if (isValidItem(stack, ModTags.Items.FORM_RIDE_CARDS)) {
                RiderManager.scheduleTicks(5, () -> RiderManager.playPublicSound(player, ModSounds.FORM_RIDE.get()));
            }
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

    private static LocalPlayer getLocalPlayer(Player player) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getUUID().equals(player.getUUID()) ? mc.player : null;
    }

    private static AbstractClientPlayer getAbstractClientPlayer(Player player) {
        return getLocalPlayer(player);
    }

    public static void setDriverAnim(ItemStack legs, ResourceLocation formId) {
        if (legs.getItem() instanceof ArcleItem arcle) {
            if (formId.equals(KuugaConfig.MIGHTY_ID) || formId.equals(KuugaConfig.GROWING_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.MIGHTY);
            } else if (formId.equals(KuugaConfig.DRAGON_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.DRAGON);
            } else if (formId.equals(KuugaConfig.PEGASUS_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.PEGASUS);
            } else if (formId.equals(KuugaConfig.TITAN_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.TITAN);
            } else if (formId.equals(KuugaConfig.RISING_MIGHTY_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_MIGHTY);
            } else if (formId.equals(KuugaConfig.RISING_DRAGON_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_DRAGON);
            } else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_PEGASUS);
            } else if (formId.equals(KuugaConfig.RISING_TITAN_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_TITAN);
            } else if (formId.equals(KuugaConfig.AMAZING_MIGHTY_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.AMAZING_MIGHTY);
            } else if (formId.equals(KuugaConfig.ULTIMATE_ID)) {
                arcle.setCurrentState(ArcleItem.AnimState.ULTIMATE);
            }
        }
    }

    private static void playHenshinSound(Player player, FormConfig form) {
        Optional<SoundEvent> sound = ModSounds.getHenshinSound(form);
        if (sound.isEmpty()) return;
        RiderManager.playPublicSound(player, sound.get());
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId, int fadeDuration) {
        PlayerAnimationTrigger.playAnimation(player, animationId, fadeDuration);
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId) {
        playAnimation(player, animationId, 0);
    }

    // 变身辅助
    private static void handleKuuga(Player player, ItemStack legs, ResourceLocation formId) {
        if (player.isCrouching()) {
            RiderManager.completeHenshin(player);
            return;
        }
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        SkillHandler.addEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 55, 4);
        SkillHandler.addResistance(player, 120);
        playAnimation(abstractClientPlayer, "kuuga_henshin");
        if (legs.getItem() instanceof ArcleItem arcleItem) {
            RiderManager.playPublicSound(player, ModSounds.ARCLE_APPEAR.get());
            if (arcleItem.getCurrentState() == ArcleItem.AnimState.IN_BODY || arcleItem.getCurrentState() == ArcleItem.AnimState.SHRINK) {
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
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        playAnimation(abstractClientPlayer, "kuuga_switch");
        RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, formId));
        FormConfig form = RiderManager.getFormConfig(player, formId);
        playHenshinSound(player, form);
        RiderManager.completeIn(90, player);
    }

    private static void handleDecade(Player player, ResourceLocation formId) {
        FormConfig form = RiderManager.getFormConfig(player, formId);
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        playAnimation(abstractClientPlayer, "decade_insert");
        RiderManager.scheduleTicks(10, () -> playHenshinSound(player, form));
        if (formId.equals(DecadeConfig.DECADE_BASE_ID)) {
            Level level = player.level();

            DecadeSpecialEffect effect = new DecadeSpecialEffect(
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
}
