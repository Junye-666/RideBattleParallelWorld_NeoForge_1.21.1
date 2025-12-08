package com.jpigeon.ridebattleparallelworlds.impl.geckoLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
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

    private GeoBone driver;
    private GeoBone body;

    @Override
    public void setCustomAnimations(BaseKamenRiderArmorItem animatable, long instanceId,
                                    AnimationState<BaseKamenRiderArmorItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (driver == null || body == null) {
            driver = this.getBone("driver").orElse(null);
            body = this.getBone("bipedBody").orElse(null);
        }

        if (driver != null && body != null) {
            applyBeltConstraint(animationState);
        }
    }

    private void applyBeltConstraint(AnimationState<BaseKamenRiderArmorItem> animationState) {
        Entity entity = animationState.getData(DataTickets.ENTITY);
        EquipmentSlot slot = animationState.getData(DataTickets.EQUIPMENT_SLOT);

        if (entity instanceof Player player && slot == EquipmentSlot.LEGS) {
            if (player.isCrouching()) {
                driver.setPosY(body.getPosY() - 1F);
                driver.setRotX(body.getRotX() - 0.4F);
            }
        }
    }

}