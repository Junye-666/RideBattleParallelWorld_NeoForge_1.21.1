package com.jpigeon.ridebattleparallelworlds.core.common.network.packet;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record PWClientStateEventPacket(String eventType, ResourceLocation riderId, ResourceLocation formId) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "pw_client_henshin_event");

    public static final Type<PWClientStateEventPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PWClientStateEventPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    PWClientStateEventPacket::eventType,
                    ResourceLocation.STREAM_CODEC,
                    PWClientStateEventPacket::riderId,
                    ResourceLocation.STREAM_CODEC,
                    PWClientStateEventPacket::formId,
                    PWClientStateEventPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
