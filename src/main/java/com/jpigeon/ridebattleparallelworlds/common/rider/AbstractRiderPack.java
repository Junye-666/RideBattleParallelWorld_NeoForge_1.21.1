package com.jpigeon.ridebattleparallelworlds.common.rider;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattlelib.common.api.registry.IRiderPack;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.api.server.ServerRiderDispatcher;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractRiderPack implements IRiderPack {
    protected abstract void initConfig();

    protected void registerSkills() {
    }

    protected Map<FormConfig, ModSounds.SoundMeta> henshinSounds() {
        return Map.of();
    }

    protected IRiderServerHandler serverHandler() {
        return null;
    }

    protected IRiderClientHandler clientHandler() {
        return null;
    }

    protected Consumer<IEventBus> extraCommon() {
        return bus -> {
        };
    }

    @Override
    public void registerCommon() {
        initConfig();
        registerSkills();
        var sounds = henshinSounds();
        if (!sounds.isEmpty()) ModSounds.registerHenshinSounds(sounds);
        var sh = serverHandler();
        if (sh != null) ServerRiderDispatcher.register(sh);
        extraCommon().accept(NeoForge.EVENT_BUS);
    }

    @Override
    public void registerClient() {
        var ch = clientHandler();
        if (ch != null) ClientRiderDispatcher.register(ch);
    }
}
