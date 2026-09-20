package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.registry.RiderPackRegistry;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.RenderHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.data.attachment.PWAttachments;
import com.jpigeon.ridebattleparallelworlds.core.common.data.component.ModDataComponents;
import com.jpigeon.ridebattleparallelworlds.core.common.debug.PWCommands;
import com.jpigeon.ridebattleparallelworlds.core.common.network.PacketHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.block.ModBlockEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.block.ModBlocks;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.PWCreativeTabs;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.pack.AgitoPack;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.pack.DecadePack;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.pack.KuugaPack;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.pack.MirrorPack;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.FormWheel;
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
        ModItems.forceInit();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);

        NeoForge.EVENT_BUS.register(FormWheel.class);
        NeoForge.EVENT_BUS.register(RenderHandler.class);

        PWAttachments.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        registerPacks();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
//        MirrorConfig.init();
//        ShockerConfig.init();
//
//        SkillHandler.registerSkillMap();
//        ModSounds.registerFormSoundMap();
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
