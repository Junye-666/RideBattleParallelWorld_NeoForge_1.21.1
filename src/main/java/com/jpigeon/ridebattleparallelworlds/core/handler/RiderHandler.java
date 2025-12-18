package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

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
    }

    @SubscribeEvent
    public static void onUnhenshin(UnhenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

        if (legs.getItem() instanceof ArcleItem arcle) {
            arcle.shrinkInBody();
        }
    }

    @SubscribeEvent
    public static void onSwitch(FormSwitchEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        ResourceLocation newFormId = event.getNewFormId();

        // 处理空我
        if (RiderManager.getActiveRiderConfig(player) == KuugaConfig.KUUGA) {
            if (player.isCrouching()) {
                RiderManager.completeHenshin(player);
                setDriverAnim(legs, newFormId);
                return;
            }
            playAnimation(abstractClientPlayer, "kuuga_switch");
            RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, newFormId));
            playHenshinSound(player, newFormId);
            RiderManager.completeIn(90, player);
        }

    }

    @SubscribeEvent
    public static void postInsert(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        setDriverAnim(legs, formId);
    }

    private static LocalPlayer getLocalPlayer(Player player){
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getUUID().equals(player.getUUID()) ? mc.player : null;
    }

    private static AbstractClientPlayer getAbstractClientPlayer(Player player){
        return getLocalPlayer(player);
    }

    public static void setDriverAnim(ItemStack legs, ResourceLocation formId) {
        if (legs.getItem() instanceof ArcleItem arcle) {
            if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId()) || formId.equals(KuugaConfig.KUUGA_GROWING_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.DRAGON);
            }
            if (formId.equals(KuugaConfig.KUUGA_PEGASUS_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.PEGASUS);
            }
            if (formId.equals(KuugaConfig.KUUGA_TITAN_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.TITAN);
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_MIGHTY_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_DRAGON_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_DRAGON);
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_PEGASUS_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_PEGASUS);
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_TITAN_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.RISING_TITAN);
            }
            if (formId.equals(KuugaConfig.KUUGA_AMAZING_MIGHTY_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.AMAZING_MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_ULTIMATE_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.ULTIMATE);
            }
        }
    }

    private static void playHenshinSound(Player player, ResourceLocation formId) {
        RiderConfig config = RiderManager.getActiveRiderConfig(player);
        if (config == null) return;
        if (config.equals(KuugaConfig.KUUGA)) {
            if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId()) || formId.equals(KuugaConfig.KUUGA_GROWING_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_MIGHTY.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_DRAGON.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_PEGASUS_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_PEGASUS.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_TITAN_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_TITAN.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_MIGHTY_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_MIGHTY.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_DRAGON_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_DRAGON.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_PEGASUS_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_PEGASUS.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_RISING_TITAN_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_TITAN.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_AMAZING_MIGHTY_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_AMAZING_MIGHTY.get());
            }
            if (formId.equals(KuugaConfig.KUUGA_ULTIMATE_FORM.getFormId())) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_ULTIMATE.get());
                player.displayClientMessage(Component.literal(player.getName().getString() + "：一条桑，请看好我最后的变身吧"), false);
            }
        }
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
            if (arcleItem.getCurrentState() == ArcleItem.AnimState.IN_BODY) {
                RiderManager.scheduleTicks(5, arcleItem::triggerAppear);
            }
            RiderManager.scheduleTicks(36, () -> playHenshinSound(player, formId));
            RiderManager.completeIn(120, player);
        }
    }
}
