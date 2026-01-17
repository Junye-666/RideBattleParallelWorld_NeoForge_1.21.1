package com.jpigeon.ridebattleparallelworlds;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SKILL_EXPLODE_GRIEF;
    public static final ModConfigSpec.IntValue RIDER_SOUNDS_VOLUME;
    public static final ModConfigSpec.BooleanValue DEBUG_MODE;

    static {
        SKILL_EXPLODE_GRIEF = BUILDER
                .comment("技能（如骑士踢）爆炸是否摧毁方块")
                .define("skillExplosionGrief", false);
        RIDER_SOUNDS_VOLUME = BUILDER
                .comment("变身时音量大小")
                .defineInRange("riderSoundsVolume", 100, 1, 100);
        DEBUG_MODE = BUILDER
                .comment("开发者向Debug模式")
                .define("debugMode", false);
    }

    static final ModConfigSpec SPEC = BUILDER.build();
}
