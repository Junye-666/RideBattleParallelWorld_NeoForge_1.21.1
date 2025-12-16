package com.jpigeon.ridebattleparallelworlds;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SKILL_EXPLODE_GRIEF;

    static {
        SKILL_EXPLODE_GRIEF = BUILDER
                .comment("技能（如骑士踢）爆炸是否摧毁方块")
                .define("skillExplosionGrief", false);
    }

    static final ModConfigSpec SPEC = BUILDER.build();
}
