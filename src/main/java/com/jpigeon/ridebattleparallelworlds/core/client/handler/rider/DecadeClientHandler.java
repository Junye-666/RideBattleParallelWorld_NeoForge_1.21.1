package com.jpigeon.ridebattleparallelworlds.core.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.DecadeAnimations;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public final class DecadeClientHandler implements IRiderClientHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRiderDispatcher.register(new DecadeClientHandler());
    }

    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    public void onPending(@NotNull ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        DecadeAnimations.INSERT_CARD.play(player);
    }

    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();
    }
}
