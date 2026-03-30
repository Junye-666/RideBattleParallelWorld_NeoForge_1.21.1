package com.jpigeon.ridebattleparallelworlds.core.common.registry.block;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.block.KuugaCoffinBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RideBattleParallelWorlds.MODID);

    public static final Supplier<BlockEntityType<KuugaCoffinBlockEntity>> KUUGA_COFFIN_BE =
            BLOCK_ENTITIES.register("kuuga_coffin_be", () -> BlockEntityType.Builder.of(
                    KuugaCoffinBlockEntity::new, ModBlocks.KUUGA_COFFIN.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
