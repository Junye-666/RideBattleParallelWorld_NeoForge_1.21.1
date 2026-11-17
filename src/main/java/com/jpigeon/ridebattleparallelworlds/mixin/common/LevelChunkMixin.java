package com.jpigeon.ridebattleparallelworlds.mixin.common;

import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorDirtyData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 捕获主世界实际发生的方块状态修改。
 */
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Inject(
            method = "setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("RETURN")
    )
    private void rbpw$trackBlockChange(
            BlockPos pos,
            BlockState newState,
            boolean isMoving,
            CallbackInfoReturnable<BlockState> cir
    ) {
        BlockState oldState = cir.getReturnValue();

        if (oldState == null || oldState.equals(newState)) {
            return;
        }

        MirrorDirtyData.markBlockChanged(
                (LevelChunk) (Object) this,
                pos,
                oldState,
                newState
        );
    }
}