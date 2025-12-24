package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

/**
 * 通用骑士实体渲染器（支持透明度）
 */
public class GenericEntityRenderer extends GeoEntityRenderer<BaseKamenRiderGeoEntity> {

    public GenericEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel model) {
        super(renderManager, model);
        this.shadowRadius = 0.0f;
    }

    @Override
    public void render(BaseKamenRiderGeoEntity entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {

        // 检查是否需要应用透明度
        if (entity.shouldApplyTransparency()) {
            float alpha = entity.getCurrentAlpha();

            // 完全透明时不渲染
            if (alpha < 0.01f) {
                return;
            }

            // 应用透明度前保存当前状态
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // 应用透明度
            poseStack.pushPose();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

            // 恢复渲染状态
            poseStack.popPose();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
        } else {
            // 正常渲染
            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @Nullable RenderType getRenderType(BaseKamenRiderGeoEntity animatable, ResourceLocation texture,
                                              @Nullable MultiBufferSource bufferSource, float partialTick) {
        // 根据是否应用透明度选择合适的渲染类型
        if (animatable.shouldApplyTransparency()) {
            return RenderType.entityTranslucent(texture);
        }
        return RenderType.entityCutoutNoCull(texture);
    }
}
