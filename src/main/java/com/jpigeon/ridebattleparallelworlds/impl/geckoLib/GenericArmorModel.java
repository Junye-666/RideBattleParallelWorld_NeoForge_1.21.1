package com.jpigeon.ridebattleparallelworlds.impl.geckoLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

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

    @Override
    public void setCustomAnimations(BaseKamenRiderArmorItem animatable, long instanceId,
                                    AnimationState<BaseKamenRiderArmorItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (driver == null) {
            driver = this.getBone("driver").orElse(null);
        }

        if (driver != null) {
            applyBeltConstraint(animationState);
        }
    }

    private void applyBeltConstraint(AnimationState<BaseKamenRiderArmorItem> animationState) {
        Entity entity = animationState.getData(DataTickets.ENTITY);
        EquipmentSlot slot = animationState.getData(DataTickets.EQUIPMENT_SLOT);

        if (entity instanceof Player player && slot == EquipmentSlot.LEGS) {
            if (player.isCrouching()) {
                Optional<GeoBone> body = getBone("bipedBody");
                body.ifPresent(
                        geoBone ->
                        {
                            driver.setPosY(geoBone.getPosY() - 1F);
                            driver.setRotX(geoBone.getRotX() - 0.4F);
                        }
                );
            }
        }
    }

}