package com.jpigeon.ridebattleparallelworlds.core.entity.custom;

import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 技能弹射物实现
 */
public class SkillProjectile extends AbstractSkillProjectile {

    private Item displayItem = Items.SNOWBALL;

    public SkillProjectile(EntityType<? extends SkillProjectile> type, Level level) {
        super(type, level);
        this.safeSetDisplayItem(Items.SNOWBALL);
    }

    public SkillProjectile(LivingEntity shooter, Level level) {
        super(ModEntities.SKILL_PROJECTILE.get(), shooter, level);
        this.safeSetDisplayItem(Items.SNOWBALL);
    }

    public SkillProjectile(Level level, double x, double y, double z) {
        super(ModEntities.SKILL_PROJECTILE.get(), x, y, z, level);
        this.safeSetDisplayItem(Items.SNOWBALL);
    }

    @Override
    protected Item getDefaultItem() {
        // 双重检查，确保绝不返回 null
        return this.displayItem != null ? this.displayItem : Items.SNOWBALL;
    }

    /**
     * 安全设置显示物品（内部使用）
     */
    private void safeSetDisplayItem(Item item) {
        this.displayItem = item != null ? item : Items.SNOWBALL;
        // 同时设置实体数据中的物品堆栈
        if (!this.level().isClientSide) {
            this.setItem(new ItemStack(this.displayItem));
        }
    }

    /**
     * 设置显示物品（公共方法）
     */
    public SkillProjectile setDisplayItem(Item item) {
        this.safeSetDisplayItem(item);
        return this;
    }

    @Override
    public void tick() {
        super.tick();

        // 生成粒子效果
        if (this.level().isClientSide && tickCount % 5 == 0) {
            spawnTrailParticles();
        }
    }

    private void spawnTrailParticles() {
        // 生成拖尾粒子
        for (int i = 0; i < 3; i++) {
            double offsetX = (this.random.nextDouble() - 0.5) * 0.1;
            double offsetY = (this.random.nextDouble() - 0.5) * 0.1;
            double offsetZ = (this.random.nextDouble() - 0.5) * 0.1;

            this.level().addParticle(
                    ParticleTypes.ASH,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    0, 0, 0
            );
        }
    }
}