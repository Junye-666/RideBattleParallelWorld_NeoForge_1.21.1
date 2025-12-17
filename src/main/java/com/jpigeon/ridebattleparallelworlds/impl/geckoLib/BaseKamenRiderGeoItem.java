package com.jpigeon.ridebattleparallelworlds.impl.geckoLib;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseKamenRiderGeoItem extends Item implements GeoItem {
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final String riderName;
    protected final String itemName;
    protected final boolean animated;
    protected final Map<String, AnimationController<BaseKamenRiderGeoItem>> controllers = new HashMap<>();

    public BaseKamenRiderGeoItem(String riderName, String itemName,  Properties properties, boolean animated) {
        super(properties);
        this.riderName = riderName;
        this.itemName = itemName;
        this.animated = animated;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registerAnimationControllers(registrar);
    }

    protected abstract void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar);

    protected void addController(AnimatableManager.ControllerRegistrar registrar, String name,
                                 AnimationController<BaseKamenRiderGeoItem> controller) {
        controllers.put(name, controller);
        registrar.add(controller);
    }

    // 创建简单的循环动画控制器
    protected AnimationController<BaseKamenRiderGeoItem> createLoopAnimationController(String name, String animationName) {
        return new AnimationController<>(this, name, 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(animationName));
            return PlayState.CONTINUE;
        });
    }

    // 创建单次播放动画控制器
    protected AnimationController<BaseKamenRiderGeoItem> createOnceAnimationController(String name, String animationName) {
        return new AnimationController<>(this, name, 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().then(animationName, Animation.LoopType.HOLD_ON_LAST_FRAME));
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
            private GeoItemRenderer<BaseKamenRiderGeoItem> renderer;

            @Override
            public GeoItemRenderer<BaseKamenRiderGeoItem> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = createRenderer();
                }
                return this.renderer;
            }
        });
    }

    protected abstract GeoItemRenderer<BaseKamenRiderGeoItem> createRenderer();

    // 资源路径生成工具方法
    protected ResourceLocation getModelPath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "geo/" + riderName.toLowerCase() + "/item/" + riderName.toLowerCase() + "_" + itemName.toLowerCase() + ".geo.json");
    }

    protected ResourceLocation getTexturePath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "textures/item/" + riderName.toLowerCase() + "/geoitem/" + riderName.toLowerCase() + "_" + itemName.toLowerCase() + ".png");
    }

    protected ResourceLocation getAnimationPath() {
        if (animated) {
            return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                    "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_" + itemName.toLowerCase() + ".animation.json");
        }
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_item.animation.json");
    }
}
