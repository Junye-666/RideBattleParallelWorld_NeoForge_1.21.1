package com.jpigeon.ridebattleparallelworlds.core.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {
    public static void deplacePlayer(Player player, double x, double y, double z, String operationType) {
        if (player instanceof LocalPlayer localPlayer && player.level().isClientSide()) {
            Vec3 movement = new Vec3(x, y, z);
            if (operationType.equals("add")) localPlayer.addDeltaMovement(movement);
            else if (operationType.equals("set")) localPlayer.setDeltaMovement(movement);
            player.hurtMarked = true;
        }
    }

}
