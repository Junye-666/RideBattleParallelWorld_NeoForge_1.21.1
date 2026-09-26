package com.jpigeon.ridebattleparallelworlds.server.util;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkillFlags;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.StormHalberdItem;
import com.jpigeon.rideevolutionlib.util.skill.SkillUtils;
import com.jpigeon.rideevolutionlib.util.state.StateFlagManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class PWSkillUtils {

    @SubscribeEvent
    public static void onSkill(SkillEvent.Post event) {
        Player player = event.getPlayer();
        ResourceLocation skillId = event.getSkillId();
        handleSkill(player, skillId);
    }

    @SubscribeEvent
    public static void onDamageEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity living)) return;
        handleDamageEntity(player, living);
    }

    @SubscribeEvent
    public static void onCollision(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (!RideBattleAPI.isTransformed(player)) return;
        handleKickCollide(player);
    }

    private static final Map<ResourceLocation, Consumer<Player>> SKILL_METHOD_MAP = new HashMap<>();

    public static void registerSkillMap(Map<ResourceLocation, Consumer<Player>> methods) {
        SKILL_METHOD_MAP.putAll(methods);
    }

    private static void handleSkill(Player player, ResourceLocation skillId) {
        Consumer<Player> consumer = SKILL_METHOD_MAP.get(skillId);
        if (consumer == null) return;
        consumer.accept(player);
        player.hurtMarked = true;
    }

    // ==========辅助方法==========
    private static void handleDamageEntity(Player player, LivingEntity living) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (RideBattleAPI.isSpecificForm(player, AgitoConfig.BURNING_ID)) {
            living.igniteForSeconds(3);
        }

        if (!StateFlagManager.activeFlags(player).isEmpty()) {
            handleBufferedDamage(player, living, mainHand, offHand);
        }
    }

    private static void handleBufferedDamage(Player player, LivingEntity living,
                                             ItemStack mainHand, ItemStack offHand) {
        if (RiderSkillFlags.isActive(player, RiderSkillFlags.SABER_SLASH)
                && mainHand.getItem() instanceof FlameSaberItem saber) {
            hurt(player, living, 30);
            saber.setClose();
            RiderSkillFlags.remove(player, RiderSkillFlags.SABER_SLASH);
        } else if (RiderSkillFlags.isActive(player, RiderSkillFlags.HALBERD_SPIN)
                && mainHand.getItem() instanceof StormHalberdItem halberd) {
            hurt(player, living, 35);
            halberd.setClose();
            RiderSkillFlags.remove(player, RiderSkillFlags.HALBERD_SPIN);
        } else if (RiderSkillFlags.isActive(player, RiderSkillFlags.FIRESTORM_ATTACK)
                && mainHand.getItem() instanceof FlameSaberItem saber
                && offHand.getItem() instanceof StormHalberdItem halberd) {
            hurt(player, living, 70);
            saber.setClose();
            halberd.setClose();
            RiderSkillFlags.remove(player, RiderSkillFlags.FIRESTORM_ATTACK);
        } else if (RiderSkillFlags.isActive(player, RiderSkillFlags.BURNING_BOMBER)
                && mainHand.getItem() instanceof ShiningCaliburItem) {
            hurt(player, living, 60);
            knockBack(player, living, 2);
            RideBattleAPI.scheduleTicks(20, () -> createExplosion(player, living, 4));
            RiderSkillFlags.remove(player, RiderSkillFlags.BURNING_BOMBER);
        } else if (RiderSkillFlags.isActive(player, RiderSkillFlags.MIGHTY_PUNCH)) {
            hurt(player, living, 15);
            knockBack(player, living, 2);
            RiderSkillFlags.remove(player, RiderSkillFlags.MIGHTY_PUNCH);
        }

        RideBattleAPI.playPublicSound(player, SoundEvents.PLAYER_ATTACK_CRIT);
    }

    private static void handleKickCollide(Player player) {
        if (!RiderSkillFlags.isActive(player, RiderSkillFlags.KICKING)) return;

        // 水平方向（避免踢到天上）
        Vec3 look = player.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize();

        // 扩展玩家碰撞盒
        AABB kickBox = player.getBoundingBox()
                .expandTowards(horizontalLook.scale(0.8))
                .inflate(0.3);

        List<LivingEntity> entities = player.level().getEntitiesOfClass(
                LivingEntity.class,
                kickBox,
                e -> e != player && e.isAlive()
        );

        if (entities.isEmpty()) return;

        ResourceLocation kickFlag = RiderSkillFlags.currentKickFlag(player);
        if (kickFlag == null) return;
        Float radius = RiderSkillFlags.KICK_RADIUS.get(kickFlag);
        if (radius == null) return;

        for (LivingEntity entity : entities) {
            if (entity.getType().equals(EntityType.ARMOR_STAND)) continue;
            createKickExplosion(player, entity, radius);
            break;   // 一帧只消耗一次
        }

        // 消费：清 KICKING + 具体 kick flag
        RiderSkillFlags.remove(player, RiderSkillFlags.KICKING);
        RiderSkillFlags.remove(player, kickFlag);
    }

    public static void addResistance(Player player, int duration) {
        addEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 4);
    }

    public static void addSaturation(Player player, int duration){
        addEffect(player, MobEffects.SATURATION, duration, 3);
    }

    public static void addRegeneration(Player player, int duration){
        addEffect(player, MobEffects.REGENERATION, duration, 5);
    }

    public static void addEffect(Player player, Holder<MobEffect> effect, int duration, int level) {
        player.addEffect(new MobEffectInstance(effect, duration, level, true, false));
    }

    public static void hurt(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.hurt(player.damageSources().playerAttack(player), amount);
        }
    }

    public static void knockBack(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.knockback(amount, -player.getLookAngle().x, -player.getLookAngle().z);
        }
    }

    public static int calculateTolerance(int origin) {
        return origin + Config.SKILL_TOLERANCE_TIME.get() * 20;
    }

    public static void createExplosion(Player player, double x, double y, double z, float radius) {
        SkillUtils.explode(player, player.level(), x, y, z, radius, radius * 4.0f);
    }

    public static void createExplosion(Player player, LivingEntity living, float radius) {
        createExplosion(player, living.xOld, living.yOld, living.zOld, radius);
    }

    public static void flagKick(Player player, int ticks) {
        scheduleTicks(10, () -> StateFlagManager.apply(player, RiderSkillFlags.KICKING, ticks));
    }

    public static void kickSequence(Player player, ResourceLocation flag, int duration) {
        flagKick(player, duration);
        RiderSkillFlags.apply(player, flag, duration);
    }

    private static void createKickExplosion(Player player, LivingEntity entity, float radius) {
        BlockPos pos = entity.getOnPos();
        // 骑士踢中心偏高（击中躯干）
        SkillUtils.explode(player, player.level(),
                pos.getX(), pos.getY() + 1.5, pos.getZ(), radius, radius * 4.0f);
    }

    private static void scheduleTicks(int ticks, Runnable callback) {
        RideBattleAPI.scheduleTicks(ticks, callback);
    }
}
