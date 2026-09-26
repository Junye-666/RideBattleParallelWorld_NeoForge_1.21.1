package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.registry.RiderPackRegistry;
import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ModDataComponents;
import com.jpigeon.ridebattleparallelworlds.common.debug.PWCommands;
import com.jpigeon.ridebattleparallelworlds.common.network.PacketHandler;
import com.jpigeon.ridebattleparallelworlds.common.registry.*;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.pack.DecadePack;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.KuugaPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.MirrorPack;
import com.jpigeon.ridebattleparallelworlds.server.handler.FormWheel;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(RideBattleParallelWorlds.MODID)
public class RideBattleParallelWorlds {
    public static final String MODID = "ridebattleparallelworlds";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RideBattleParallelWorlds(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(PacketHandler::register);

        PWCreativeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);

        NeoForge.EVENT_BUS.register(FormWheel.class);

        PWAttachments.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        registerPacks();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void registerPacks() {
        registerPack(new KuugaPack());
        registerPack(new AgitoPack());
        registerPack(new MirrorPack());

        registerPack(new DecadePack());
    }

    private void registerPack(IRiderPack riderPack) {
        RiderPackRegistry.register(riderPack);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        PWCommands.register(event.getDispatcher());
    }
}
