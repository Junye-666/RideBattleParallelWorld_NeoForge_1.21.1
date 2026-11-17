package com.jpigeon.ridebattleparallelworlds.common.data.component;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RideBattleParallelWorlds.MODID);

    /**
     * 物品变换 数据
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemData>> ORIGIN_ITEM_DATA =
            register("origin_item_data",
            builder -> builder
                    .persistent(ItemData.CODEC)
    );

    /**
     * 物品归属者 数据
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ITEM_OWNER =
            register("item_owner", builder -> builder
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC));

    /**
     * 物品状态数据
     * <p>
     * 每个 ItemStack 独立保存自己的状态。
     * 支持存档持久化和客户端同步。
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>>
            ITEM_STATE = register(
            "item_state",
            builder -> builder
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator) {

        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
