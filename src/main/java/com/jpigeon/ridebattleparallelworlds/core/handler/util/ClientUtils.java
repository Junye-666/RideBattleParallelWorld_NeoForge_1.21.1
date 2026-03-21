package com.jpigeon.ridebattleparallelworlds.core.handler.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static void deplacePlayer(LocalPlayer player, double x, double y, double z, String operationType) {
        Vec3 movement = new Vec3(x, y, z);
        if (operationType.equals("add")) player.addDeltaMovement(movement);
        else if (operationType.equals("set")) player.setDeltaMovement(movement);
    }
}
