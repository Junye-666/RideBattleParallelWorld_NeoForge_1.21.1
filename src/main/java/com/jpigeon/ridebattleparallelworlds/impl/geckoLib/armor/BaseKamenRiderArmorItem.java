package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.armor;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseKamenRiderArmorItem extends ArmorItem implements GeoItem {
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final String riderName;
    protected final String formName;
    protected final boolean animated;
    protected final Map<String, AnimationController<BaseKamenRiderArmorItem>> controllers = new HashMap<>();

    public BaseKamenRiderArmorItem(String riderName, String formName, Holder<ArmorMaterial> material, Type type, Properties properties, boolean animated) {
        super(material, type, properties.stacksTo(1));
        this.riderName = riderName;
        this.formName = formName;
        this.animated = animated;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registerAnimationControllers(registrar);
    }

    protected abstract void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar);

    protected void addController(AnimatableManager.ControllerRegistrar registrar, String name,
                                 AnimationController<BaseKamenRiderArmorItem> controller) {
        controllers.put(name, controller);
        registrar.add(controller);
    }

    // 创建简单的循环动画控制器
    protected AnimationController<BaseKamenRiderArmorItem> createLoopAnimationController(String name, String animationName) {
        return new AnimationController<>(this, name, 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(animationName));
            return PlayState.CONTINUE;
        });
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T livingEntity, ItemStack itemStack,
                    @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = createRenderer();
                }
                return this.renderer;
            }
        });
    }

    protected abstract GeoArmorRenderer<?> createRenderer();

    // 资源路径生成工具方法
    protected ResourceLocation getModelPath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "geo/" + riderName.toLowerCase() + "/armor/" + riderName.toLowerCase() + "_" + formName.toLowerCase() + ".geo.json");

    }

    protected ResourceLocation getTexturePath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "textures/armor/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_" + formName.toLowerCase() + ".png");
    }

    protected ResourceLocation getAnimationPath() {
        if (animated) {
            return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                    "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_" + formName.toLowerCase() + ".animation.json");
        }
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_armor.animation.json");
    }
}