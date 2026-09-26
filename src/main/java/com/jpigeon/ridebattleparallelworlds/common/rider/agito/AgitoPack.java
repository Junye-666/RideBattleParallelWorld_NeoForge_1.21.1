package com.jpigeon.ridebattleparallelworlds.common.rider.agito;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatcher;
import com.jpigeon.ridebattleparallelworlds.client.handler.rider.AgitoClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.server.handler.rider.AgitoServerHandler;
import net.minecraft.resources.ResourceLocation;

public class AgitoPack implements IRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    public void registerCommon() {
        AgitoConfig.init();

        AgitoSkills.register();

        ServerRiderDispatcher.register(new AgitoServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new AgitoClientHandler());
    }
}
