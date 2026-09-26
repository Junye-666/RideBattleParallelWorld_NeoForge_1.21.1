package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatcher;
import com.jpigeon.ridebattleparallelworlds.client.handler.rider.MirrorClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.server.handler.rider.MirrorServerHandler;
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

        ServerRiderDispatcher.register(new MirrorServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new MirrorClientHandler());
    }
}
