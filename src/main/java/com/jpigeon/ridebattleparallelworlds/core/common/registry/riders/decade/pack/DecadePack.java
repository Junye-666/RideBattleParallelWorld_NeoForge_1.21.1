package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.pack;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatchers;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.rider.DecadeClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.rider.DecadeServerHandler;
import net.minecraft.resources.ResourceLocation;

public class DecadePack implements IRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    public void registerCommon() {
        DecadeConfig.init();
        DecadeSounds.init();

        // TODO: Decade skills
        ModSounds.registerHenshinSounds(DecadeSounds.HENSHIN_MAP);

        ServerRiderDispatchers.register(new DecadeServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new DecadeClientHandler());
    }
}
