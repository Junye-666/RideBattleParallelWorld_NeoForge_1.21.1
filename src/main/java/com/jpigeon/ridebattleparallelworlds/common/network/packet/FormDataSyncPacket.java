package com.jpigeon.ridebattleparallelworlds.common.network.packet;

import com.jpigeon.ridebattleparallelworlds.common.data.attachment.PWData;
import com.jpigeon.ridebattleparallelworlds.common.network.PWPacket;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record FormDataSyncPacket(UUID playerId, PWData data) implements PWPacket {
    public static final ResourceLocation ID = PWPacket.ofPath("pw_data_sync");

    public static final StreamCodec<RegistryFriendlyByteBuf, FormDataSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    FormDataSyncPacket::playerId,
                    ByteBufCodecs.fromCodecWithRegistries(PWData.CODEC),
                    FormDataSyncPacket::data,
                    FormDataSyncPacket::new
            );

    public static final Type<FormDataSyncPacket> TYPE = new Type<>(ID);

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
