package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.AnimationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;

import static software.bernie.geckolib.animation.Animation.LoopType.*;

public abstract class BaseKamenRiderBlock extends BlockEntity implements GeoBlockEntity {
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final String riderName;
    protected final String blockName;
    protected final boolean animated;
    protected final AnimationManager<BaseKamenRiderBlock> animationManager;
    protected final Map<String, AnimationController<BaseKamenRiderBlock>> controllers = new HashMap<>();

    public BaseKamenRiderBlock(String riderName, String formName, BlockEntityType<?> type, BlockPos pos, BlockState blockState, boolean animated) {
        super(type, pos, blockState);
        this.riderName = riderName;
        this.blockName = formName;
        this.animated = animated;
        this.animationManager = new AnimationManager<>(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registerAnimationControllers(registrar);
    }

    protected abstract void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar);

    protected void addController(AnimatableManager.ControllerRegistrar registrar, String name,
                                 AnimationController<BaseKamenRiderBlock> controller) {
        controllers.put(name, controller);
        animationManager.registerController(name, controller);
        registrar.add(controller);
    }

    public void setAnimState(String state) {
        animationManager.setState(state);
    }

    public String getCurrentAnimState() {
        return animationManager.getCurrentState();
    }

    protected AnimationController<BaseKamenRiderBlock> createLoopController(
            String animationName) {
        return createStateController(animationName, LOOP);
    }

    protected AnimationController<BaseKamenRiderBlock> createOnceController(
            String animationName) {
        return createStateController(animationName, PLAY_ONCE);
    }

    protected AnimationController<BaseKamenRiderBlock> createHoldController(
            String animationName) {
        return createStateController(animationName, HOLD_ON_LAST_FRAME);
    }

    protected AnimationController<BaseKamenRiderBlock> createStateController(
            String animationName, Animation.LoopType loopType) {
        return new AnimationController<>(this, animationName + "_controller", 0, state -> {
            // 只有当管理器当前状态匹配时才播放动画
            if (animationManager.getCurrentState().equals(animationName)) {
                if (loopType.equals(LOOP)) {
                    state.getController().setAnimation(RawAnimation.begin().thenLoop(animationName));
                } else if (loopType.equals(PLAY_ONCE)) {
                    state.getController().setAnimation(RawAnimation.begin().then(animationName, PLAY_ONCE));
                } else if (loopType.equals(HOLD_ON_LAST_FRAME)) {
                    state.getController().setAnimation(RawAnimation.begin().then(animationName, HOLD_ON_LAST_FRAME));
                }
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected abstract GeoArmorRenderer<?> createRenderer();

    // 资源路径生成工具方法
    protected ResourceLocation getModelPath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "geo/" + riderName.toLowerCase() + "/block/" + riderName.toLowerCase() + "_" + blockName.toLowerCase() + ".geo.json");

    }

    protected ResourceLocation getTexturePath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "textures/block/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_" + blockName.toLowerCase() + ".png");
    }

    protected ResourceLocation getAnimationPath() {
        if (animated) {
            return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                    "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_" + blockName.toLowerCase() + ".animation.json");
        }
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "animations/" + riderName.toLowerCase() + "/" + riderName.toLowerCase() + "_block.animation.json");
    }
}