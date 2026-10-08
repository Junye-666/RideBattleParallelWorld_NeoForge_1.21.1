package com.jpigeon.ridebattleparallelworlds.common.rider.decade;

import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.AbstractRiderPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.handler.DecadeClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.handler.DecadeServerHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class DecadePack extends AbstractRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    protected void initConfig() {
        DecadeConfig.init();
    }

    @Override
    protected Map<FormConfig, ModSounds.SoundMeta> henshinSounds() {
        return DecadeSounds.HENSHIN_MAP;
    }

    @Override
    protected void registerSkills() {
        // TODO: Decade skills
        super.registerSkills();
    }

    @Override
    protected IRiderServerHandler serverHandler() {
        return new DecadeServerHandler();
    }

    @Override
    protected IRiderClientHandler clientHandler() {
        return new DecadeClientHandler();
    }

}
