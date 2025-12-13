package com.jpigeon.ridebattleparallelworlds.core.sound;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

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


    private static Supplier<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
