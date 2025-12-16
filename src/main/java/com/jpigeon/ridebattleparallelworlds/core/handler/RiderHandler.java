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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
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
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        // 处理空我
        if (event.getRiderId().equals(RiderIds.KUUGA_ID)) {
            if (player.isCrouching()) {
                RiderManager.completeHenshin(player);
                return;
            }
            SkillHandler.addEffect(player, MobEffects.MOVEMENT_SLOWDOWN, 55, 4);
            SkillHandler.addResistance(player, 120);
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            if (legs.getItem() instanceof ArcleItem arcleItem) {
                RiderManager.playPublicSound(player, ModSounds.ARCLE_APPEAR.get());
                RiderManager.scheduleTicks(5, arcleItem::triggerAppear);
                ResourceLocation formId = event.getFormId();
                RiderManager.scheduleTicks(36, () -> playHenshinSound(player, formId));
                RiderManager.completeIn(120, player);
            }
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
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_switch", 0);
            RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, newFormId));
            playHenshinSound(player, newFormId);
            RiderManager.completeIn(90, player);
        }

    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
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
            }
        }
    }


}
