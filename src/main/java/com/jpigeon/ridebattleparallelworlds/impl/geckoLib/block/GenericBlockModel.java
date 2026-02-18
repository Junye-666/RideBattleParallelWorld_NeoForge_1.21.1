package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;

public class GenericBlockModel extends GeoModel<BaseKamenRiderBlock> {
    private final ResourceLocation modelPath;
    private final ResourceLocation texturePath;
    private final ResourceLocation animationPath;

    public GenericBlockModel(ResourceLocation modelPath, ResourceLocation texturePath,
                             ResourceLocation animationPath) {
        this.modelPath = modelPath;
        this.texturePath = texturePath;
        this.animationPath = animationPath;
    }

    @Override
    public ResourceLocation getModelResource(BaseKamenRiderBlock armorItem) {
        return modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(BaseKamenRiderBlock armorItem) {
        return texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(BaseKamenRiderBlock armorItem) {
        return animationPath;
    }
}