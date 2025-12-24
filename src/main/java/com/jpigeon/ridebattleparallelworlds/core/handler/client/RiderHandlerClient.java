package com.jpigeon.ridebattleparallelworlds.core.handler.client;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.handler.RiderHandler;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RiderHandlerClient {
    @SubscribeEvent
    public static void onPlayerLoggedIn(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof Player player) {
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            if (legs.getItem() instanceof ArcleItem arcle) {
                if (!RiderManager.isTransformed(player)) {
                    arcle.shrinkInBody();
                    return;
                }
                ResourceLocation formId = RiderManager.getCurrentFormId(player);
                if (formId == null) return;
                RiderHandler.setDriverAnim(legs, formId);
            }
        }

    }
}
