package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.pack;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatchers;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.rider.MirrorClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.rider.MirrorServerHandler;
import net.minecraft.resources.ResourceLocation;

public class MirrorPack implements IRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    public void registerCommon() {
        MirrorConfig.init();
        // TODO: Mirror sounds

        // TODO: Mirror skills

        ServerRiderDispatchers.register(new MirrorServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new MirrorClientHandler());
    }
}
