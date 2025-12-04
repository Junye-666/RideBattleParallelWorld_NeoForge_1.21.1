package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderHandler {
    @SubscribeEvent
    public static void onHenshin(HenshinEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer abstractClientPlayer = null;
        if (mc.player != null && mc.player.getUUID().equals(event.getPlayer().getUUID())) {
            abstractClientPlayer = mc.player;
        }
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

        // 处理空我
        if (event.getRiderId().equals(RiderIds.KUUGA_ID)) {
            event.getPlayer().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 55, 4));
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            if (legs.getItem() instanceof ArcleItem arcleItem) {
                RiderManager.scheduleTicks(10,
                        arcleItem::triggerAppear
                );
                RiderManager.scheduleTicks(45,
                        () -> RiderManager.completeHenshin(event.getPlayer())
                );
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
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer abstractClientPlayer = null;
        if (mc.player != null && mc.player.getUUID().equals(event.getPlayer().getUUID())) {
            abstractClientPlayer = mc.player;
        }
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

        if (event.getNewFormId().equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())) {
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            if (legs.getItem() instanceof ArcleItem arcleItem) {
                RiderManager.scheduleTicks(10,
                        arcleItem::triggerAppear
                );
                RiderManager.scheduleTicks(45,
                        () -> RiderManager.completeHenshin(event.getPlayer())
                );
            }
        }
        if (event.getNewFormId().equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId())) {
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            if (legs.getItem() instanceof ArcleItem arcleItem) {
                RiderManager.scheduleTicks(10,
                        arcleItem::triggerAppear
                );
                RiderManager.scheduleTicks(45,
                        () -> RiderManager.completeHenshin(event.getPlayer())
                );
            }
        }
    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof ArcleItem arcle) {
            ResourceLocation formId = event.getFormId();
            if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId())){
                arcle.setCurrentState(ArcleItem.AnimState.MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())){
                arcle.setCurrentState(ArcleItem.AnimState.DRAGON);
            }

        }
    }

    @SubscribeEvent
    public static void postSwitch(FormSwitchEvent.Post event){
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        if (legs.getItem() instanceof ArcleItem arcle) {
            ResourceLocation formId = event.getNewFormId();
            if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId())){
                arcle.setCurrentState(ArcleItem.AnimState.MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())){
                arcle.setCurrentState(ArcleItem.AnimState.DRAGON);
            }

        }
    }
}
