package com.jpigeon.ridebattleparallelworlds.common.network.packet;

import com.jpigeon.ridebattleparallelworlds.common.network.PWPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 龙骑系卡牌系统的全部网络包。
 * <p>
 * C→S：OpenDeckPacket / SaveDeckOrderPacket / DrawCardPacket
 * S→C：DeckSyncPacket
 * <p>
 * 抽卡记录（drawn）不走 S→C 同步，客户端仅用于 UI 置灰展示，
 * 通过 DeckSyncPacket 一次性带给屏幕。
 */
public final class DeckPackets {
    private DeckPackets() {
    }

    // ==================== C → S ====================

    /**
     * 玩家潜行 + 右键卡盒 → 请求打开界面。
     */
    public record OpenDeckPacket(ResourceLocation riderId) implements PWPacket {
        public static final ResourceLocation ID = PWPacket.ofPath("deck_open");

        public static final StreamCodec<RegistryFriendlyByteBuf, OpenDeckPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC, OpenDeckPacket::riderId,
                        OpenDeckPacket::new);

        public static final Type<OpenDeckPacket> TYPE = new Type<>(ID);

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

    /**
     * 玩家在界面里调整顺序后关闭 → 保存。
     */
    public record SaveDeckOrderPacket(ResourceLocation riderId,
                                      List<ResourceLocation> order) implements PWPacket {
        public static final ResourceLocation ID = PWPacket.ofPath("deck_save_order");

        public static final StreamCodec<RegistryFriendlyByteBuf, SaveDeckOrderPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC, SaveDeckOrderPacket::riderId,
                        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SaveDeckOrderPacket::order,
                        SaveDeckOrderPacket::new);

        public static final Type<SaveDeckOrderPacket> TYPE = new Type<>(ID);

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

    /**
     * 玩家按抽卡键 → 请求抽取下一张卡。
     */
    public record DrawCardPacket(ResourceLocation riderId) implements PWPacket {
        public static final ResourceLocation ID = PWPacket.ofPath("deck_draw");

        public static final StreamCodec<RegistryFriendlyByteBuf, DrawCardPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC, DrawCardPacket::riderId,
                        DrawCardPacket::new);

        public static final Type<DrawCardPacket> TYPE = new Type<>(ID);

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

    // ==================== S → C ====================

    /**
     * 服务端下发：打开界面的初始化数据。
     */
    public record DeckSyncPacket(ResourceLocation riderId,
                                 List<ResourceLocation> order,
                                 Set<ResourceLocation> drawn) implements PWPacket {
        public static final ResourceLocation ID = PWPacket.ofPath("deck_sync");

        public static final StreamCodec<RegistryFriendlyByteBuf, DeckSyncPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC, DeckSyncPacket::riderId,
                        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), DeckSyncPacket::order,
                        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list())
                                .map(HashSet::new, ArrayList::new),
                        DeckSyncPacket::drawn,
                        DeckSyncPacket::new);

        public static final Type<DeckSyncPacket> TYPE = new Type<>(ID);

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}