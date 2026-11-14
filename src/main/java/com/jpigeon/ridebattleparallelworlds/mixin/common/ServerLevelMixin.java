package com.jpigeon.ridebattleparallelworlds.mixin.common;

import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    // 计划刻方块 tick（红石、活塞、观察者、TNT 等）
    @Inject(method = "tickBlock", at = @At("HEAD"), cancellable = true)
    private void rbw$skipBlockTick(BlockPos pos, Block block, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    // 计划刻流体 tick（水/岩浆流动）
    @Inject(method = "tickFluid", at = @At("HEAD"), cancellable = true)
    private void rbw$skipFluidTick(BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    // 随机刻（冰融化、雪堆积、草蔓延、树叶衰变、作物生长）
    @Inject(method = "tickChunk", at = @At("HEAD"), cancellable = true)
    private void rbw$skipRandomTicks(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    //  邻居更新（防止直接调用绕过计划刻）
    @Inject(method = "updateNeighborsAt", at = @At("HEAD"), cancellable = true)
    private void rbw$skipUpdateNeighbors(BlockPos pos, Block block, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    @Inject(method = "updateNeighborsAtExceptFromFacing", at = @At("HEAD"), cancellable = true)
    private void rbw$skipUpdateNeighborsExcept(BlockPos pos, Block block, Direction skipSide, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    @Inject(method = "neighborChanged(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;)V",
            at = @At("HEAD"), cancellable = true)
    private void rbw$skipNeighborChanged(BlockPos pos, Block block, BlockPos fromPos, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }

    @Inject(method = "neighborChanged(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;Z)V",
            at = @At("HEAD"), cancellable = true)
    private void rbw$skipNeighborChanged2(BlockState state, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving, CallbackInfo ci) {
        if (MirrorConstants.isMirror((ServerLevel)(Object)this)) ci.cancel();
    }
}