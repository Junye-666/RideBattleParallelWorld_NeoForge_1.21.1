package com.jpigeon.ridebattleparallelworlds.common.rider.decade.pack;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatcher;
import com.jpigeon.ridebattleparallelworlds.client.handler.rider.DecadeClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.server.handler.rider.DecadeServerHandler;
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

        ServerRiderDispatcher.register(new DecadeServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new DecadeClientHandler());
    }
}
