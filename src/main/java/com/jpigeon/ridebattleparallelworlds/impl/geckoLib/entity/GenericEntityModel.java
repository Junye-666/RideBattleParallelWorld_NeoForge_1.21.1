package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * 通用骑士实体模型
 */
public class GenericEntityModel extends GeoModel<BaseKamenRiderGeoEntity> {
    private final ResourceLocation modelPath;
    private final ResourceLocation texturePath;
    private final ResourceLocation animationPath;

    public GenericEntityModel(ResourceLocation modelPath, ResourceLocation texturePath,
                              ResourceLocation animationPath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.animationPath = animationPath;
    }

    @Override
    public ResourceLocation getModelResource(BaseKamenRiderGeoEntity entity) {
        return modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(BaseKamenRiderGeoEntity entity) {
        return texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(BaseKamenRiderGeoEntity entity) {
        return animationPath;
    }
}
