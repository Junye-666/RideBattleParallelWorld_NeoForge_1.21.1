package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public final class KuugaSounds {
    private KuugaSounds() {
    }

    public static final Map<FormConfig, ModSounds.SoundMeta> HENSHIN_MAP = new HashMap<>();

    /**
     * 由 KuugaPack.registerCommon 调用（在 KuugaConfig.init 之后）
     */
    public static void init() {
        put(KuugaConfig.KUUGA_GROWING_FORM, ModSounds.KUUGA_MIGHTY.get());
        put(KuugaConfig.KUUGA_MIGHTY_FORM, ModSounds.KUUGA_MIGHTY.get());
        put(KuugaConfig.KUUGA_DRAGON_FORM, ModSounds.KUUGA_DRAGON.get());
        put(KuugaConfig.KUUGA_PEGASUS_FORM, ModSounds.KUUGA_PEGASUS.get());
        put(KuugaConfig.KUUGA_TITAN_FORM, ModSounds.KUUGA_TITAN.get());
        put(KuugaConfig.KUUGA_RISING_MIGHTY_FORM, ModSounds.KUUGA_RISING_MIGHTY.get());
        put(KuugaConfig.KUUGA_RISING_DRAGON_FORM, ModSounds.KUUGA_RISING_DRAGON.get());
        put(KuugaConfig.KUUGA_RISING_PEGASUS_FORM, ModSounds.KUUGA_RISING_PEGASUS.get());
        put(KuugaConfig.KUUGA_RISING_TITAN_FORM, ModSounds.KUUGA_RISING_TITAN.get());
        put(KuugaConfig.KUUGA_AMAZING_MIGHTY_FORM, ModSounds.KUUGA_AMAZING_MIGHTY.get());
        put(KuugaConfig.KUUGA_ULTIMATE_FORM, ModSounds.KUUGA_ULTIMATE.get());
    }

    private static void put(FormConfig form, SoundEvent sound) {
        HENSHIN_MAP.put(form, new ModSounds.SoundMeta(sound, 75));
    }
}
