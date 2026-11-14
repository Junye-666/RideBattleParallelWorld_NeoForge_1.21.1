package com.jpigeon.ridebattleparallelworlds.mixin.client;

import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    // 1) renderSky 前禁用剔除（防止 X 缩放导致行列式反 → 天空被剔除）
    @Inject(method = "renderSky", at = @At("HEAD"))
    private void rbw$beforeSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                               float partialTick, Camera camera, boolean isFoggy,
                               Runnable skyFogSetup, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && MirrorConstants.isMirror(level)) {
            RenderSystem.disableCull();
        }
    }

    // 2) renderSky 结束后恢复剔除
    @Inject(method = "renderSky", at = @At("RETURN"))
    private void rbw$afterSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                              float partialTick, Camera camera, boolean isFoggy,
                              Runnable skyFogSetup, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && MirrorConstants.isMirror(level)) {
            RenderSystem.enableCull();
        }
    }

    // 3) frustumMatrix X 反转
    @ModifyVariable(method = "renderSky", at = @At("HEAD"), argsOnly = true, index = 1)
    private Matrix4f rbw$mirrorSkyMatrix(Matrix4f frustumMatrix) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && MirrorConstants.isMirror(level)) {
            return new Matrix4f(frustumMatrix).scale(-1.0F, 1.0F, 1.0F);
        }
        return frustumMatrix;
    }
}