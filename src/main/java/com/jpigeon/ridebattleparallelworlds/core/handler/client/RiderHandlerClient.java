package com.jpigeon.ridebattleparallelworlds.core.handler.client;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.ItemGrantEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.extra.shocker.ShockerCombatManItem;
import com.jpigeon.ridebattleparallelworlds.core.handler.RiderHandler;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RiderHandlerClient {
    @SubscribeEvent
    public static void onPlayerLoggedIn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            switch (legs.getItem()) {
                case ArcleItem arcle -> {
                    if (!RiderManager.isTransformed(player)) {
                        arcle.shrinkInBody();
                        return;
                    }
                    ResourceLocation formId = RiderManager.getCurrentFormId(player);
                    if (formId == null) return;
                    RiderHandler.setDriverAnim(legs, formId);
                }
                case AlterRingItem alterRingItem -> {
                    if (!RiderManager.isTransformed(player)) {
                        alterRingItem.shrinkInBody();
                        return;
                    }
                    ResourceLocation formId = RiderManager.getCurrentFormId(player);
                    if (formId == null) return;
                    RiderHandler.setDriverAnim(legs, formId);
                }
                case DecaDriverItem decaDriver -> {
                    if (!RiderManager.isDriverEmpty(player)) decaDriver.triggerOpen();
                }
                default -> {
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEquip(LivingEquipmentChangeEvent event) {
        ItemStack stack = event.getTo();
        if (!(event.getEntity() instanceof Player player)) return;
        switch (stack.getItem()) {
            case DecaDriverItem decaDriver -> decaDriver.triggerOpen();
            case ArcleItem arcle -> {
                if (!RiderManager.isTransformed(player)) {
                    arcle.shrinkInBody();
                }
            }
            case AlterRingItem alterRing -> {
                if (!RiderManager.isTransformed(player)) {
                    alterRing.shrinkInBody();
                }
            }
            case ShockerCombatManItem ignored -> RiderManager.transform(player);
            default -> {
            }
        }
    }

    @SubscribeEvent
    public static void onGrantItem(ItemGrantEvent.Post event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof FlameSaberItem flameSaber) {
            flameSaber.setClose();
        }
    }
}
