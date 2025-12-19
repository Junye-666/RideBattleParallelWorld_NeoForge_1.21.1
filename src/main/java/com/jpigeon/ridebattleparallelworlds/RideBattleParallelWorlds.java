package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattleparallelworlds.core.component.ModDataComponents;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.handler.AbilitiesHandler;
import com.jpigeon.ridebattleparallelworlds.core.handler.FormWheel;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.item.PWCreativeTabs;
import com.jpigeon.ridebattleparallelworlds.core.network.PWPacketHandler;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(RideBattleParallelWorlds.MODID)
public class RideBattleParallelWorlds {
    public static final String MODID = "ridebattleparallelworlds";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RideBattleParallelWorlds(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(PWPacketHandler::register);
        NeoForge.EVENT_BUS.register(this);

        PWCreativeTabs.CREATIVE_MODE_TAB.register(modEventBus);

        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);

        NeoForge.EVENT_BUS.register(FormWheel.class);
        NeoForge.EVENT_BUS.register(AbilitiesHandler.class);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
