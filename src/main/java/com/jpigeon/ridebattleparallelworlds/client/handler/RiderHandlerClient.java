package com.jpigeon.ridebattleparallelworlds.client.handler;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.client.render.ClientMirrorColorCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RiderHandlerClient {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.displayClientMessage(
                Component.translatable("message.riderGreet.login"),
                false
        );

        player.displayClientMessage(
                Component.translatable("message.riderHint.login")
                        .withStyle(ChatFormatting.GREEN),
                false
        );

        player.displayClientMessage(
                Component.translatable("message.fromHint.login")
                        .withStyle(ChatFormatting.RED),
                false
        );
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientMirrorColorCache.clear();
    }
}
