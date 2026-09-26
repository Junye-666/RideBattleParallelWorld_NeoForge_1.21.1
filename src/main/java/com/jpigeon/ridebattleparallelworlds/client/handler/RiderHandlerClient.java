package com.jpigeon.ridebattleparallelworlds.client.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.ItemGrantEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.armor.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.armor.VBuckleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.jpigeon.rideevolutionlib.util.api.DriverItemStateUtil.setDriverAnim;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RiderHandlerClient {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.displayClientMessage(Component.translatable("message.riderGreet.login"), false);
        player.displayClientMessage(Component.translatable("message.riderHint.login").withStyle(ChatFormatting.GREEN), false);
        player.displayClientMessage(Component.translatable("message.fromHint.login").withStyle(ChatFormatting.RED), false);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        switch (legs.getItem()) {
            case ArcleItem arcle -> {
                if (!isTransformed(player)) {
                    arcle.shrinkInBody();
                    return;
                }
                ResourceLocation formId = RideBattleAPI.getCurrentFormId(player);
                if (formId == null) return;
                setDriverAnim(legs, formId);
            }
            case AlterRingItem alterRingItem -> {
                if (!isTransformed(player)) {
                    alterRingItem.shrinkInBody();
                    return;
                }
                ResourceLocation formId = RideBattleAPI.getCurrentFormId(player);
                if (formId == null) return;
                setDriverAnim(legs, formId);
            }
            case DecaDriverItem decaDriver -> {
                if (!RideBattleAPI.isDriverEmpty(player)) decaDriver.triggerOpen();
            }
            case VBuckleItem vBuckle -> vBuckle.setIdle();
            default -> {
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEquip(LivingEquipmentChangeEvent event) {
        EquipmentSlot slot = event.getSlot();
        Item item = event.getTo().getItem();
        if (!(event.getEntity() instanceof Player player)) return;

        if (slot.isArmor()) {
            if (!isTransformed(player))
                switch (item) {
                    case DecaDriverItem decaDriver -> decaDriver.triggerOpen();
                    case ArcleItem arcle -> arcle.shrinkInBody();
                    case AlterRingItem alterRing -> alterRing.shrinkInBody();
                    case VBuckleItem vBuckle -> vBuckle.triggerAppear();
                    default -> {
                    }
                }
        }
    }

    @SubscribeEvent
    public static void onGrantItem(ItemGrantEvent.Post event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof FlameSaberItem flameSaber) {
            flameSaber.setClose();
        }
        if (stack.getItem() instanceof ShiningCaliburItem shiningCalibur) {
            shiningCalibur.setClose();
        }
    }

    private static boolean isTransformed(Player player) {
        return RideBattleAPI.isTransformed(player);
    }
}
