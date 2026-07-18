package com.jpigeon.ridebattleparallelworlds.core.common.registry;

import com.jpigeon.ridebattleparallelworlds.core.client.handler.RenderHandler;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.SkillHandlerClient;

public class RegistryUtils {
    public static void registerClientMaps() {
        RenderHandler.registerDeckFormMap();
        SkillHandlerClient.registerSkillMap();
    }
}
