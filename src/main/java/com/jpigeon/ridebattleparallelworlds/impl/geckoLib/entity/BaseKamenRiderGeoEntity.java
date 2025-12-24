package com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseKamenRiderGeoEntity extends LivingEntity implements GeoEntity {
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final String riderName;
    protected final String entityName;
    protected final boolean hasGravity;
    protected final boolean isInvulnerable;
    protected final Map<String, AnimationController<BaseKamenRiderGeoEntity>> controllers = new HashMap<>();

    public BaseKamenRiderGeoEntity(EntityType<? extends LivingEntity> entityType, Level level,
                                   String riderName, String entityName,
                                   boolean hasGravity, boolean isInvulnerable) {
        super(entityType, level);
        this.riderName = riderName;
        this.entityName = entityName;
        this.hasGravity = hasGravity;
        this.isInvulnerable = isInvulnerable;

        // 根据参数设置实体属性
        if (!hasGravity) {
            this.setNoGravity(true);
        }
        if (isInvulnerable) {
            this.setInvulnerable(true);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registerAnimationControllers(registrar);
    }

    /**
     * 子类必须实现的动画控制器注册方法
     */
    protected abstract void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar);

    /**
     * 添加动画控制器并存储引用
     */
    protected void addController(AnimatableManager.ControllerRegistrar registrar, String name,
                                 AnimationController<BaseKamenRiderGeoEntity> controller) {
        controllers.put(name, controller);
        registrar.add(controller);
    }

    /**
     * 获取指定的动画控制器
     */
    @Nullable
    public AnimationController<BaseKamenRiderGeoEntity> getController(String name) {
        return controllers.get(name);
    }

    /**
     * 创建简单的循环动画控制器
     */
    protected AnimationController<BaseKamenRiderGeoEntity> createLoopAnimationController(
            String name, String animationName) {
        return new AnimationController<>(this, name, 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().thenLoop(animationName));
            return PlayState.CONTINUE;
        });
    }

    /**
     * 创建单次播放动画控制器
     */
    protected AnimationController<BaseKamenRiderGeoEntity> createOnceAnimationController(
            String name, String animationName) {
        return new AnimationController<>(this, name, 0, state -> {
            state.getController().setAnimation(
                    RawAnimation.begin().then(animationName, Animation.LoopType.HOLD_ON_LAST_FRAME)
            );
            return PlayState.CONTINUE;
        });
    }

    //========== 资源路径生成工具方法 ==========

    /**
     * 获取模型资源路径
     */
    protected ResourceLocation getModelPath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "geo/" + riderName.toLowerCase() + "/entity/" +
                        riderName.toLowerCase() + "_" + entityName.toLowerCase() + ".geo.json");
    }

    /**
     * 获取纹理资源路径
     */
    protected ResourceLocation getTexturePath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "textures/entity/" + riderName.toLowerCase() + "/" +
                        riderName.toLowerCase() + "_" + entityName.toLowerCase() + ".png");
    }

    /**
     * 获取动画资源路径
     */
    protected ResourceLocation getAnimationPath() {
        return ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID,
                "animations/" + riderName.toLowerCase() + "/entity/" +
                        riderName.toLowerCase() + "_" + entityName.toLowerCase() + ".animation.json");
    }

    //========== GeckoLib接口实现 ==========

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object entity) {
        return tickCount;
    }

    //========== 实体基础方法 ==========

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z);
    }

    //========== 可选的透明度支持 ==========

    /**
     * 获取当前透明度（0.0-1.0），子类可重写
     */
    public float getCurrentAlpha() {
        return 1.0f;
    }

    /**
     * 是否应用透明度效果
     */
    public boolean shouldApplyTransparency() {
        return false;
    }
}
