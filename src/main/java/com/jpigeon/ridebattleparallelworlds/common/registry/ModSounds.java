package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, RideBattleParallelWorlds.MODID);

    public static final Supplier<SoundEvent> ARCLE_APPEAR = registerSoundEvent("arcle_appear");
    public static final Supplier<SoundEvent> KUUGA_MIGHTY = registerSoundEvent("kuuga_mighty");
    public static final Supplier<SoundEvent> KUUGA_DRAGON = registerSoundEvent("kuuga_dragon");
    public static final Supplier<SoundEvent> KUUGA_PEGASUS = registerSoundEvent("kuuga_pegasus");
    public static final Supplier<SoundEvent> KUUGA_TITAN = registerSoundEvent("kuuga_titan");
    public static final Supplier<SoundEvent> KUUGA_RISING_MIGHTY = registerSoundEvent("kuuga_rising_mighty");
    public static final Supplier<SoundEvent> KUUGA_RISING_DRAGON = registerSoundEvent("kuuga_rising_dragon");
    public static final Supplier<SoundEvent> KUUGA_RISING_PEGASUS = registerSoundEvent("kuuga_rising_pegasus");
    public static final Supplier<SoundEvent> KUUGA_RISING_TITAN = registerSoundEvent("kuuga_rising_titan");
    public static final Supplier<SoundEvent> KUUGA_AMAZING_MIGHTY = registerSoundEvent("kuuga_amazing_mighty");
    public static final Supplier<SoundEvent> KUUGA_ULTIMATE = registerSoundEvent("kuuga_ultimate");

    public static final Supplier<SoundEvent> AGITO_PREPARE = registerSoundEvent("agito_prepare");
    public static final Supplier<SoundEvent> AGITO_STEADY = registerSoundEvent("agito_steady");
    public static final Supplier<SoundEvent> AGITO_FINISH = registerSoundEvent("agito_finish");

    public static final Supplier<SoundEvent> SUMMON_V_BUCKLE = registerSoundEvent("summon_v_buckle");
    public static final Supplier<SoundEvent> MIRROR_HENSHIN = registerSoundEvent("mirror_henshin");

    public static final Supplier<SoundEvent> DECADE_INSERT = registerSoundEvent("decade_insert");
    public static final Supplier<SoundEvent> KAMEN_RIDE = registerSoundEvent("kamen_ride");
    public static final Supplier<SoundEvent> KR_DECADE = registerSoundEvent("kr_decade");
    public static final Supplier<SoundEvent> KR_KUUGA = registerSoundEvent("kr_kuuga");
    public static final Supplier<SoundEvent> KR_AGITO = registerSoundEvent("kr_agito");

    public static final Supplier<SoundEvent> FORM_RIDE = registerSoundEvent("form_ride");
    public static final Supplier<SoundEvent> FR_KUUGA_DRAGON = registerSoundEvent("fr_kuuga_dragon");
    public static final Supplier<SoundEvent> FR_KUUGA_PEGASUS = registerSoundEvent("fr_kuuga_pegasus");
    public static final Supplier<SoundEvent> FR_KUUGA_TITAN = registerSoundEvent("fr_kuuga_titan");
    public static final Supplier<SoundEvent> FR_AGITO_FLAME = registerSoundEvent("fr_agito_flame");
    public static final Supplier<SoundEvent> FR_AGITO_STORM = registerSoundEvent("fr_agito_storm");
    public static final Supplier<SoundEvent> FR_AGITO_BURNING = registerSoundEvent("fr_agito_burning");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    private static final Map<FormConfig, SoundEvent> RIDER_HENSHIN_SOUNDS = new HashMap<>();
    private static final Map<SoundEvent, Integer> SOUNDS_LENGTH = new HashMap<>();

    /**
     * 供 Pack 调用：注册一批变身音效
     */
    public static void registerHenshinSounds(Map<FormConfig, SoundMeta> sounds) {
        sounds.forEach((form, meta) -> {
            RIDER_HENSHIN_SOUNDS.put(form, meta.sound());
            SOUNDS_LENGTH.put(meta.sound(), meta.length());
        });
    }

    public record SoundMeta(SoundEvent sound, int length) {
    }

    public static Optional<SoundEvent> getHenshinSound(FormConfig form) {
        return Optional.ofNullable(RIDER_HENSHIN_SOUNDS.get(form));
    }

    public static Optional<Integer> getSoundLength(FormConfig form) {
        return getHenshinSound(form).flatMap(s -> Optional.ofNullable(SOUNDS_LENGTH.get(s)));
    }
}
