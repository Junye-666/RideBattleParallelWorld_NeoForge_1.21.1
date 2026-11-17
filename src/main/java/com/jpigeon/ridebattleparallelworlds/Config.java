package com.jpigeon.ridebattleparallelworlds;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SKILL_EXPLODE_GRIEF;
    public static final ModConfigSpec.IntValue SKILL_TOLERANCE_TIME;
    public static final ModConfigSpec.IntValue RIDER_SOUNDS_VOLUME;
    public static final ModConfigSpec.IntValue MIRROR_Y_BOTTOM;
    public static final ModConfigSpec.IntValue MIRROR_SCAN_RADIUS;
    public static final ModConfigSpec.BooleanValue DEBUG_MODE;

    static {
        SKILL_EXPLODE_GRIEF = BUILDER
                .comment("技能（如骑士踢）爆炸是否摧毁方块")
                .define("skillExplosionGrief", false);
        SKILL_TOLERANCE_TIME = BUILDER
                .comment("技能额外容错（释放后额外有效/实际持续）时间（秒）")
                .defineInRange("skillToleranceTime", 1, 0, 2);
        RIDER_SOUNDS_VOLUME = BUILDER
                .comment("变身时音量大小")
                .defineInRange("riderSoundsVolume", 100, 1, 100);
        MIRROR_Y_BOTTOM = BUILDER
                .comment("镜世界底部 Y 坐标（低于此值的区域不会生成镜像）")
                .defineInRange("mirrorYBottom", -56, -64, 320);
        MIRROR_SCAN_RADIUS = BUILDER
                .comment("镜世界单次扫描半径（区块数），值越大单次加载范围越广，但生成耗时和存档体积会相应增加")
                .defineInRange("mirrorScanRadius", 2, 1, 8);
        DEBUG_MODE = BUILDER
                .comment("开发者向Debug模式")
                .define("debugMode", false);
    }

    public static boolean debug() {
        return DEBUG_MODE.get();
    }

    static final ModConfigSpec SPEC = BUILDER.build();
}
