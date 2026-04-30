package com.jpigeon.ridebattleparallelworlds.core.common.registry;

import com.jpigeon.ridebattleparallelworlds.core.client.RenderHandler;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.handler.SkillHandler;

public class RegistryUtils {
    public static void registerMaps() {
        SkillHandler.registerSkillMap();
        ModSounds.registerFormSoundMap();
        RenderHandler.registerDeckFormMap();
    }
}
