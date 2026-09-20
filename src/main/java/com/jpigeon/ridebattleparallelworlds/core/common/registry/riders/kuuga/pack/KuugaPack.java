package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.pack;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatchers;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.rider.KuugaClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.rider.KuugaServerHandler;
import net.minecraft.resources.ResourceLocation;

public class KuugaPack implements IRiderPack {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.KUUGA_ID;
    }

    @Override
    public void registerCommon() {
        // 1. 配置
        KuugaConfig.init();
        KuugaSounds.init();

        // 2. 技能注册
        KuugaSkills.register();

        // 3. 音效注册
        ModSounds.registerHenshinSounds(KuugaSounds.HENSHIN_MAP);

        // 4. 服务端 Handler
        ServerRiderDispatchers.register(new KuugaServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new KuugaClientHandler());
    }
}
