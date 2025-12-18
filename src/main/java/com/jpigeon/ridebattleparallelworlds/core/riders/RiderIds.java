package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.resources.ResourceLocation;

public class RiderIds {
    public static final ResourceLocation KUUGA_ID = fromString("kuuga");
    public static final ResourceLocation AGITO_ID = fromString("agito");
    public static final ResourceLocation RYUKI_ID = fromString("ryuki");
    public static final ResourceLocation FAIZ_ID = fromString("faiz");
    public static final ResourceLocation BLADE_ID = fromString("blade");
    public static final ResourceLocation HIBIKI_ID = fromString("hibiki");
    public static final ResourceLocation KABUTO_ID = fromString("kabuto");
    public static final ResourceLocation DEN_O_ID = fromString("den_o");
    public static final ResourceLocation KIVA_ID = fromString("kiva");
    public static final ResourceLocation DECADE_ID = fromString("decade");

    public static ResourceLocation fromString(String id) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, id);
    }
}
