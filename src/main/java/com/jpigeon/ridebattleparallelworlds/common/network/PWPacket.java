package com.jpigeon.ridebattleparallelworlds.common.network;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public interface PWPacket extends CustomPacketPayload {
    ResourceLocation id();

    @Override
    default Type<? extends CustomPacketPayload> type() {
        return new Type<>(id());
    }

    static ResourceLocation ofPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, path);
    }
}
