package com.jpigeon.ridebattleparallelworlds.common.network.packet;

import com.jpigeon.ridebattleparallelworlds.common.network.PWPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record MirrorGrassColorSyncPacket(int grassColor) implements PWPacket {
    public static final ResourceLocation ID = PWPacket.ofPath("mirror_grass_color_sync");
    public static final CustomPacketPayload.Type<MirrorGrassColorSyncPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MirrorGrassColorSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, MirrorGrassColorSyncPacket::grassColor,
                    MirrorGrassColorSyncPacket::new
            );

    @Override
    public ResourceLocation id() {
        return ID;
    }
}