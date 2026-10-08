package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga;

import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.AbstractRiderPack;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.handler.KuugaClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.handler.KuugaServerHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class KuugaPack extends AbstractRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.KUUGA_ID;
    }

    @Override
    protected void initConfig() {
        KuugaConfig.init();
    }

    @Override
    protected Map<FormConfig, ModSounds.SoundMeta> henshinSounds() {
        return KuugaSounds.HENSHIN_MAP;
    }

    @Override
    protected void registerSkills() {
        KuugaSkills.register();
    }

    @Override
    protected IRiderServerHandler serverHandler() {
        return new KuugaServerHandler();
    }

    @Override
    protected IRiderClientHandler clientHandler() {
        return new KuugaClientHandler();
    }
}
