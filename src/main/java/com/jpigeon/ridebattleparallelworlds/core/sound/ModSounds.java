package com.jpigeon.ridebattleparallelworlds.core.sound;

import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
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


    private static Supplier<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }

    private static final Map<FormConfig, SoundEvent> RIDER_HENSHIN_SOUNDS = new HashMap<>();
    private static final Map<SoundEvent, Integer> SOUNDS_LENGTH = new HashMap<>();

    public static void registerHenshinSound(FormConfig form, SoundEvent sound, int length) {
        RIDER_HENSHIN_SOUNDS.put(form, sound);
        SOUNDS_LENGTH.put(sound, length);
    }

    public static Optional<SoundEvent> getHenshinSound(FormConfig form) {
        return Optional.ofNullable(RIDER_HENSHIN_SOUNDS.get(form));
    }

    public static Optional<Integer> getSoundLength(SoundEvent sound) {
        return Optional.ofNullable(SOUNDS_LENGTH.get(sound));
    }

    public static Optional<Integer> getSoundLength(FormConfig form) {
        SoundEvent sound = getHenshinSound(form).isPresent() ? getHenshinSound(form).get() : null;
        return getSoundLength(sound);
    }

    public static void registerFormSoundMap() {
        registerHenshinSound(KuugaConfig.KUUGA_GROWING_FORM, KUUGA_MIGHTY.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_MIGHTY_FORM, KUUGA_MIGHTY.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_DRAGON_FORM, KUUGA_DRAGON.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_PEGASUS_FORM, KUUGA_PEGASUS.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_TITAN_FORM, KUUGA_TITAN.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_RISING_MIGHTY_FORM, KUUGA_RISING_MIGHTY.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_RISING_DRAGON_FORM, KUUGA_RISING_DRAGON.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_RISING_PEGASUS_FORM, KUUGA_RISING_PEGASUS.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_RISING_TITAN_FORM, KUUGA_RISING_TITAN.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_AMAZING_MIGHTY_FORM, KUUGA_AMAZING_MIGHTY.get(), 120);
        registerHenshinSound(KuugaConfig.KUUGA_ULTIMATE_FORM, KUUGA_ULTIMATE.get(), 120);

        registerHenshinSound(DecadeConfig.DECADE_BASE, KR_DECADE.get(), 65);
        registerHenshinSound(DecadeConfig.DECADE_KUUGA_MIGHTY, KR_KUUGA.get(), 65);
        registerHenshinSound(DecadeConfig.DECADE_KUUGA_DRAGON, FR_KUUGA_DRAGON.get(), 90);
        registerHenshinSound(DecadeConfig.DECADE_KUUGA_PEGASUS, FR_KUUGA_PEGASUS.get(), 90);
        registerHenshinSound(DecadeConfig.DECADE_KUUGA_TITAN, FR_KUUGA_TITAN.get(), 90);

        registerHenshinSound(DecadeConfig.DECADE_AGITO_GROUND, KR_AGITO.get(), 65);
        registerHenshinSound(DecadeConfig.DECADE_AGITO_FLAME, FR_AGITO_FLAME.get(), 70);
        registerHenshinSound(DecadeConfig.DECADE_AGITO_STORM, FR_AGITO_STORM.get(), 70);
        registerHenshinSound(DecadeConfig.DECADE_AGITO_BURNING, FR_AGITO_BURNING.get(), 70);

    }

}
