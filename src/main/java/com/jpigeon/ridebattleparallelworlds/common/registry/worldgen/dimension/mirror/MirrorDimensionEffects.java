package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class MirrorDimensionEffects extends DimensionSpecialEffects {

    public MirrorDimensionEffects() {
        // 参数：云层高度、是否有天空、天空类型、是否始终环境光、是否雾
        super(192.0F, true, SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
        // 如果希望雾的颜色也镜像，可以在这里调整
        return biomeFogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }
}
