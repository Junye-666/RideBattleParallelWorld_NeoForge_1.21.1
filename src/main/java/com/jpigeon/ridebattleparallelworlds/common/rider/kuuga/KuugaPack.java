package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatcher;
import com.jpigeon.ridebattleparallelworlds.client.handler.rider.KuugaClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.server.handler.rider.KuugaServerHandler;
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
        ServerRiderDispatcher.register(new KuugaServerHandler());
    }

    @Override
    public void registerClient() {
        ClientRiderDispatcher.register(new KuugaClientHandler());
    }
}
