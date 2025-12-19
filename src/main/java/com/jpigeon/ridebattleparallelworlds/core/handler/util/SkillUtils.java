package com.jpigeon.ridebattleparallelworlds.core.handler.util;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.SkillProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

/**
 * 弹射物发射器工具类
 */
public class SkillUtils {

    /**
     * 创建并发射一个标准弹射物
     */
    public static SkillProjectile launchStandard(LivingEntity shooter, float speed, float damage) {
        Level level = shooter.level();
        if (level.isClientSide) return null;

        SkillProjectile projectile = new SkillProjectile(shooter, level);
        configureStandard(projectile, damage);
        launchProjectile(projectile, shooter, speed);

        return projectile;
    }

    /**
     * 创建并发射爆炸弹射物
     */
    public static SkillProjectile launchExplosive(LivingEntity shooter, float speed, float damage, float explosionPower) {
        Level level = shooter.level();
        if (level.isClientSide) return null;

        SkillProjectile projectile = new SkillProjectile(shooter, level);
        configureExplosive(projectile, damage, explosionPower);
        launchProjectile(projectile, shooter, speed);

        return projectile;
    }

    /**
     * 创建并发射火焰弹射物
     */
    public static SkillProjectile launchFireball(LivingEntity shooter, float speed, float damage, float explosionPower) {
        Level level = shooter.level();
        if (level.isClientSide) return null;

        SkillProjectile projectile = new SkillProjectile(shooter, level);
        configureFireball(projectile, damage, explosionPower);
        launchProjectile(projectile, shooter, speed);

        return projectile;
    }

    /**
     * 自定义弹射物
     */
    /**
     * 自定义弹射物
     */
    public static SkillProjectile launchCustom(LivingEntity shooter, float speed,
                                               Consumer<SkillProjectile> configurator) {
        Level level = shooter.level();
        if (level.isClientSide) return null;

        SkillProjectile projectile = new SkillProjectile(shooter, level);

        // 确保有默认的显示物品
        projectile.setDisplayItem(Items.SNOWBALL);

        // 应用自定义配置
        if (configurator != null) {
            try {
                configurator.accept(projectile);
            } catch (Exception e) {
                RideBattleParallelWorlds.LOGGER.error("Error configuring projectile: {}", e.getMessage());
                e.printStackTrace();
            }
        }

        launchProjectile(projectile, shooter, speed);

        return projectile;
    }

    // ===== 配置预设 =====

    private static void configureStandard(SkillProjectile projectile, float damage) {
        projectile.setDisplayItem(Items.SNOWBALL)
                .setBaseDamage(damage)
                .setExplosionPower(0)
                .setCausesFire(false)
                .setLifeTime(60)
                .setGravity(0.03f)
                ;
    }

    private static void configureExplosive(SkillProjectile projectile, float damage, float explosionPower) {
        projectile.setDisplayItem(Items.FIRE_CHARGE)
                .setBaseDamage(damage)
                .setExplosionPower(explosionPower)
                .setCausesFire(false)
                .setLifeTime(40)
                .setGravity(0.02f)
                ;
    }

    private static void configureFireball(SkillProjectile projectile, float damage, float explosionPower) {
        projectile.setDisplayItem(Items.FIRE_CHARGE)
                .setBaseDamage(damage * 0.5f) // 减少直接伤害
                .setExplosionPower(explosionPower)
                .setCausesFire(true)
                .setLifeTime(50)
                .setGravity(0.01f)

                .onHitEntity((proj, target) -> target.setRemainingFireTicks(100));
    }

    // ===== 发射逻辑 =====

    private static void launchProjectile(SkillProjectile projectile, LivingEntity shooter, float speed) {
        Vec3 look = shooter.getLookAngle();

        // 设置位置
        projectile.setPos(
                shooter.getX() + look.x,
                shooter.getY() + shooter.getEyeHeight() - 0.1,
                shooter.getZ() + look.z
        );

        // 设置速度
        projectile.shoot(look.x, look.y, look.z, speed, 1.0f);

        // 触发生成回调
        projectile.triggerOnSpawn();

        // 添加到世界
        shooter.level().addFreshEntity(projectile);
    }
}