package com.jpigeon.ridebattleparallelworlds.core.common.network.packet;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerMovementPacket(UUID playerId, Double x, Double y, Double z, String operationType) implements CustomPacketPayload{
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "pw_player_movement");

    public static final CustomPacketPayload.Type<PlayerMovementPacket> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMovementPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    PlayerMovementPacket::playerId,
                    ByteBufCodecs.DOUBLE,
                    PlayerMovementPacket::x,
                    ByteBufCodecs.DOUBLE,
                    PlayerMovementPacket::y,
                    ByteBufCodecs.DOUBLE,
                    PlayerMovementPacket::z,
                    ByteBufCodecs.STRING_UTF8,
                    PlayerMovementPacket::operationType,
                    PlayerMovementPacket::new
            );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
