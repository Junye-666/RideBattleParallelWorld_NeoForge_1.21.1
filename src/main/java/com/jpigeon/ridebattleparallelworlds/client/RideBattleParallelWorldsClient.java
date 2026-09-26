package com.jpigeon.ridebattleparallelworlds.client;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.client.render.RenderHandler;
import com.jpigeon.ridebattleparallelworlds.common.registry.RegistryUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = RideBattleParallelWorlds.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RideBattleParallelWorldsClient {
    public RideBattleParallelWorldsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.register(RenderHandler.class);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        RegistryUtils.registerClientMaps();
    }
}
