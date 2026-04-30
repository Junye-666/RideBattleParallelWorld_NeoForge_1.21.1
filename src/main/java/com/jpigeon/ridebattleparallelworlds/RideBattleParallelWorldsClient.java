package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattleparallelworlds.core.common.registry.RegistryUtils;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.extra.shocker.ShockerConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = RideBattleParallelWorlds.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RideBattleParallelWorldsClient {
    public RideBattleParallelWorldsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        RiderSkills.init();
        KuugaConfig.init();
        AgitoConfig.init();
        MirrorConfig.init();
        DecadeConfig.init();
        ShockerConfig.init();

        RegistryUtils.registerMaps();
    }
}
