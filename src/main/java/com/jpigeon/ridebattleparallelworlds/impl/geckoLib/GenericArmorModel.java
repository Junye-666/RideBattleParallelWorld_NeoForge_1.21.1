package com.jpigeon.ridebattleparallelworlds.impl.geckoLib;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GenericArmorModel extends GeoModel<BaseKamenRiderArmorItem> {
    private final ResourceLocation modelPath;
    private final ResourceLocation texturePath;
    private final ResourceLocation animationPath;

    public GenericArmorModel(ResourceLocation modelPath, ResourceLocation texturePath,
                             ResourceLocation animationPath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.animationPath = animationPath;
    }

    @Override
    public ResourceLocation getModelResource(BaseKamenRiderArmorItem armorItem) {
        return modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(BaseKamenRiderArmorItem armorItem) {
        return texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(BaseKamenRiderArmorItem armorItem) {
        return animationPath;
    }
}