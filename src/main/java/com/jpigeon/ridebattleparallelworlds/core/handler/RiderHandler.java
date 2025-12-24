package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;
import com.jpigeon.ridebattlelib.core.system.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.DecadeSpecialEffect;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
            ResourceLocation actualFormId = FormWheel.KUUGA_FORM_ID_WHEEL.get(FormWheel.getCurrentIndex());
            FormWheel.setArcleSlot(player, actualFormId);
            handleKuuga(player, legs, actualFormId);
        }
        if (event.getRiderId().equals(RiderIds.DECADE_ID)) {
            handleDecade(event.getPlayer(), legs, formId);
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
        ResourceLocation newFormId = event.getNewFormId();

        // 处理空我
        if (RiderManager.getActiveRiderConfig(player) == KuugaConfig.KUUGA) {
            handleKuugaSwitch(player, legs, newFormId);
        }
        if (RiderManager.getActiveRiderConfig(player) == DecadeConfig.DECADE) {
            handleDecade(event.getPlayer(), legs, newFormId);
        }

    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        setDriverAnim(legs, formId);
    }

    @SubscribeEvent
    public static void onInsert(ItemInsertionEvent.Pre event) {
        if (event.getSlotId().equals(DecadeConfig.DECA_CARD)) {
            ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

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

    private static void playHenshinSound(Player player, ResourceLocation formId) {
        if (KuugaConfig.KUUGA.includesFormId(formId)) {
            if (formId.equals(KuugaConfig.MIGHTY_ID) || formId.equals(KuugaConfig.GROWING_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_MIGHTY.get());
            } else if (formId.equals(KuugaConfig.DRAGON_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_DRAGON.get());
            } else if (formId.equals(KuugaConfig.PEGASUS_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_PEGASUS.get());
            } else if (formId.equals(KuugaConfig.TITAN_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_TITAN.get());
            } else if (formId.equals(KuugaConfig.RISING_MIGHTY_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_MIGHTY.get());
            } else if (formId.equals(KuugaConfig.RISING_DRAGON_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_DRAGON.get());
            } else if (formId.equals(KuugaConfig.RISING_PEGASUS_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_PEGASUS.get());
            } else if (formId.equals(KuugaConfig.RISING_TITAN_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_RISING_TITAN.get());
            } else if (formId.equals(KuugaConfig.AMAZING_MIGHTY_ID)) {
                RiderManager.playPublicSound(player, ModSounds.KUUGA_AMAZING_MIGHTY.get());
            } else if (formId.equals(KuugaConfig.ULTIMATE_ID)) {
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
            if (arcleItem.getCurrentState() == ArcleItem.AnimState.IN_BODY || arcleItem.getCurrentState() == ArcleItem.AnimState.SHRINK) {
                RiderManager.scheduleTicks(5, arcleItem::triggerAppear);
            }
            RiderManager.scheduleTicks(36, () -> playHenshinSound(player, formId));
            RiderManager.completeIn(120, player);
        }
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
        playHenshinSound(player, formId);
        RiderManager.completeIn(90, player);
    }

    private static void handleDecade(Player player, ItemStack legs, ResourceLocation formId) {
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            decaDriver.triggerClose();
        }
        playHenshinSound(player, formId);
        AbstractClientPlayer abstractClientPlayer = getAbstractClientPlayer(player);
        playAnimation(abstractClientPlayer, "decade_insert");
        if (formId.equals(DecadeConfig.DECADE_BASE_ID)){
            Level level = player.level();
            DecadeSpecialEffect effect = new DecadeSpecialEffect(
                    ModEntities.DECADE_SPECIAL_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setXRot(player.getXRot());
            effect.setYRot(player.getYRot());
            effect.setYBodyRot(player.getYRot());
            effect.setYHeadRot(player.getYHeadRot());

            effect.setOwner(player);

            if (level.addFreshEntity(effect)){
                RiderManager.completeIn(20, player);
                RiderManager.scheduleTicks(25, effect::discard);
            }
        }
        if (formId.equals(KuugaConfig.MIGHTY_ID) || formId.equals(KuugaConfig.DRAGON_ID) || formId.equals(KuugaConfig.PEGASUS_ID) || formId.equals(KuugaConfig.TITAN_ID)) {
            RiderManager.completeIn(90, player);
        }
    }
}
