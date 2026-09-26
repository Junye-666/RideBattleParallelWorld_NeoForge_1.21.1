package com.jpigeon.ridebattleparallelworlds.common.rider.decade.pack;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecadeConfig;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class DecadeSounds {
    private DecadeSounds() {
    }

    public static final Map<FormConfig, ModSounds.SoundMeta> HENSHIN_MAP = new HashMap<>();

    public static void init() {
        put(DecadeConfig.DECADE_BASE, ModSounds.KR_DECADE.get(), 65);

        // Kuuga
        put(DecadeConfig.DECADE_KUUGA_MIGHTY, ModSounds.KR_KUUGA.get(), 65);
        put(DecadeConfig.DECADE_KUUGA_DRAGON, ModSounds.FR_KUUGA_DRAGON.get(), 90);
        put(DecadeConfig.DECADE_KUUGA_PEGASUS, ModSounds.FR_KUUGA_PEGASUS.get(), 90);
        put(DecadeConfig.DECADE_KUUGA_TITAN, ModSounds.FR_KUUGA_TITAN.get(), 90);

        // Agito
        put(DecadeConfig.DECADE_AGITO_GROUND, ModSounds.KR_AGITO.get(), 65);
        put(DecadeConfig.DECADE_AGITO_FLAME, ModSounds.FR_AGITO_FLAME.get(), 70);
        put(DecadeConfig.DECADE_AGITO_STORM, ModSounds.FR_AGITO_STORM.get(), 70);
        put(DecadeConfig.DECADE_AGITO_BURNING, ModSounds.FR_AGITO_BURNING.get(), 70);
    }

    private static void put(FormConfig form, SoundEvent sound, int length) {
        HENSHIN_MAP.put(form, new ModSounds.SoundMeta(sound, length));
    }
}
