package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki;

import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.AbstractRiderPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.handler.MirrorClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.handler.MirrorServerHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;

public class MirrorPack extends AbstractRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    protected void initConfig() {
        MirrorConfig.init();
    }

    @Override
    protected void registerSkills() {
        MirrorSkills.register();
    }

    @Override
    protected IRiderServerHandler serverHandler() {
        NeoForge.EVENT_BUS.addListener(MirrorServerHandler::onExtractPre);

        return new MirrorServerHandler();
    }

    @Override
    protected IRiderClientHandler clientHandler() {
        return new MirrorClientHandler();
    }
}
