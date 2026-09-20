package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.pack;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatchers;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.rider.AgitoClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.rider.AgitoServerHandler;
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

        ServerRiderDispatchers.register(new AgitoServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new AgitoClientHandler());
    }
}
