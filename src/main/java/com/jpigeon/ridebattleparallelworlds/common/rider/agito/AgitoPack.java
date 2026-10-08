package com.jpigeon.ridebattleparallelworlds.common.rider.agito;

import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.AbstractRiderPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.handler.AgitoClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.handler.AgitoServerHandler;
import net.minecraft.resources.ResourceLocation;

public class AgitoPack extends AbstractRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    protected void initConfig() {
        AgitoConfig.init();
    }

    @Override
    protected void registerSkills() {
        AgitoSkills.register();
    }

    @Override
    protected IRiderServerHandler serverHandler() {
        return new AgitoServerHandler();
    }

    @Override
    protected IRiderClientHandler clientHandler() {
        return new AgitoClientHandler();

    }
}
