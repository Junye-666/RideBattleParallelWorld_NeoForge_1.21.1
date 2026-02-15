package com.jpigeon.ridebattleparallelworlds.core.network.packet;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PWAnimationPacket(UUID playerId, String animationId, Integer fadeDuration) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "pw_animation");

    public static final Type<PWAnimationPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PWAnimationPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    PWAnimationPacket::playerId,
                    ByteBufCodecs.STRING_UTF8,
                    PWAnimationPacket::animationId,
                    ByteBufCodecs.INT,
                    PWAnimationPacket::fadeDuration,
                    PWAnimationPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
