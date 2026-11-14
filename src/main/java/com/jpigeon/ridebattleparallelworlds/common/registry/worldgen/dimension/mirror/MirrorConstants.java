package com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror;

import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MirrorConstants {
    private MirrorConstants() {}

    public static final ResourceKey<Level> MIRROR_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mirror_world"));

    public static final int BOUNCE_THRESHOLD = 2;
    public static final int REPULSION = 3;
    public static final int CORROSION_DURATION_SECONDS = 600;
    public static final int BFS_LIMIT = 4096;

    // === 改成方法，按调用时读 Config ===
    public static int yBottom() {
        return Config.MIRROR_Y_BOTTOM.get();
    }

    public static int scanRadius() {
        return Config.MIRROR_SCAN_RADIUS.get() * 16;
    }

    public static boolean isMirror(Level level) {
        return level.dimension().equals(MIRROR_WORLD);
    }
}