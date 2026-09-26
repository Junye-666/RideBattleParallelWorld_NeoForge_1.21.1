package com.jpigeon.ridebattleparallelworlds.common.rider;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.resources.ResourceLocation;

public class RiderIds {
    public static final ResourceLocation KUUGA_ID = id("kuuga");
    public static final ResourceLocation AGITO_ID = id("agito");
    public static final ResourceLocation MIRROR_SYSTEM_ID = id("mirror");
    public static final ResourceLocation FAIZ_ID = id("faiz");
    public static final ResourceLocation BLADE_ID = id("blade");
    public static final ResourceLocation HIBIKI_ID = id("hibiki");
    public static final ResourceLocation KABUTO_ID = id("kabuto");
    public static final ResourceLocation DEN_O_ID = id("den_o");
    public static final ResourceLocation KIVA_ID = id("kiva");
    public static final ResourceLocation DECADE_ID = id("decade");

    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, id);
    }
}
