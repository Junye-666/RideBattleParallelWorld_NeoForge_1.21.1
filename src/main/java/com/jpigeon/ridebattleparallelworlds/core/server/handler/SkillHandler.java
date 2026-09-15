package com.jpigeon.ridebattleparallelworlds.core.server.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.network.packet.PWClientSkillPacket;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkillFlags;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.entity.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item.StormHalberdItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.RisingDragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.server.util.ProjectileUtils;
import com.jpigeon.rideevolutionlib.util.SkillUtils;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class SkillHandler {

    @SubscribeEvent
    public static void onSkill(SkillEvent.Post event) {
        Player player = event.getPlayer();
        ResourceLocation skillId = event.getSkillId();
        handleSkill(player, skillId);
        sendSkillEventToClient(player, skillId);
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
        if (!RideBattleAPI.isTransformed(player)) return;
        if (player.level().isClientSide()) return;
        handleKickCollide(player);
    }

    private static final Map<ResourceLocation, Consumer<Player>> SKILL_METHOD_MAP = new HashMap<>();

    public static void registerSkillMap() {
        SKILL_METHOD_MAP.put(RiderSkills.GROWING_KICK, SkillHandler::growingKick);
        SKILL_METHOD_MAP.put(RiderSkills.MIGHTY_KICK, SkillHandler::mightyKick);
        SKILL_METHOD_MAP.put(RiderSkills.SPLASH_DRAGON, SkillHandler::splashDragon);
        SKILL_METHOD_MAP.put(RiderSkills.BLAST_PEGASUS, SkillHandler::blastPegasus);
        SKILL_METHOD_MAP.put(RiderSkills.CALAMITY_TITAN, SkillHandler::calamityTitan);
        SKILL_METHOD_MAP.put(RiderSkills.RISING_MIGHTY_KICK, SkillHandler::risingMightyKick);
        SKILL_METHOD_MAP.put(RiderSkills.RISING_SPLASH_DRAGON, SkillHandler::risingSplashDragon);
        SKILL_METHOD_MAP.put(RiderSkills.RISING_BLAST_PEGASUS, SkillHandler::risingBlastPegasus);
        SKILL_METHOD_MAP.put(RiderSkills.RISING_CALAMITY_TITAN, SkillHandler::risingCalamityTitan);
        SKILL_METHOD_MAP.put(RiderSkills.AMAZING_MIGHTY_KICK, SkillHandler::amazingMightyKick);
        SKILL_METHOD_MAP.put(RiderSkills.ULTIMATE_KICK, SkillHandler::ultimateKick);
        SKILL_METHOD_MAP.put(RiderSkills.GROUND_KICK, SkillHandler::groundKick);
        SKILL_METHOD_MAP.put(RiderSkills.FLAME_SABER, SkillHandler::flameSaber);
        SKILL_METHOD_MAP.put(RiderSkills.STORM_HALBERD, SkillHandler::stormHalberd);
        SKILL_METHOD_MAP.put(RiderSkills.TRINITY_WEAPON, SkillHandler::trinityWeapon);
        SKILL_METHOD_MAP.put(RiderSkills.SHINING_CALIBUR, SkillHandler::shiningCalibur);
    }

    private static void handleSkill(Player player, ResourceLocation skillId) {
        Consumer<Player> consumer = SKILL_METHOD_MAP.get(skillId);
        if (consumer == null) return;
        consumer.accept(player);
        player.hurtMarked = true;
    }

    // ==========技能业务逻辑==========
    // 空我
    private static void growingKick(Player player) {
        int duration = calculateTolerance(40);

        addResistance(player, duration);

        kickSequence(player, RiderSkillFlags.GROWING_KICK, duration);
    }

    private static void mightyKick(Player player) {
        int duration = calculateTolerance(40);

        addResistance(player, duration);

        kickSequence(player, RiderSkillFlags.MIGHTY_KICK, duration);
    }

    private static void splashDragon(Player player) {
        addResistance(player, 30);
        double distance;

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (mainHand.getItem() instanceof DragonRodItem) {
            distance = 2.0;
        } else if (offHand.getItem() instanceof DragonRodItem) {
            distance = 1.5;
        } else {
            distance = 0;
        }
        scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1.5 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                3));
    }

    private static void blastPegasus(Player player) {
        addResistance(player, 20);

        scheduleTicks(10, () ->
                ProjectileUtils.launchCustom(player, 3.0F, skillProjectile ->
                        skillProjectile.setDisplayItem(ModItems.PEGASUS_ELEMENT.get())
                                .setBaseDamage(2)
                                .setExplosionPower(3)
                                .setGravity(0)
                                .setLifeTime(100)
                                .onHitEntity((proj, target) -> {
                                    if (target instanceof LivingEntity living) {
                                        living.addEffect(new MobEffectInstance(
                                                MobEffects.MOVEMENT_SLOWDOWN, 100, 2
                                        ));
                                    }
                                })));
    }

    private static void calamityTitan(Player player) {
        addResistance(player, 20);

        double distance = 1.5;
        scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                3));
    }

    private static void risingMightyKick(Player player) {
        int duration = calculateTolerance(40);

        addResistance(player, duration);

        kickSequence(player, RiderSkillFlags.RISING_MIGHTY_KICK, duration);
    }

    private static void risingSplashDragon(Player player) {
        addResistance(player, 30);
        double distance;

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (mainHand.getItem() instanceof RisingDragonRodItem) {
            distance = 2.5;
        } else if (offHand.getItem() instanceof RisingDragonRodItem) {
            distance = 2.0;
        } else {
            distance = 0;
        }
        scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1.5 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                4));
    }

    private static void risingBlastPegasus(Player player) {
        addResistance(player, 30);

        addResistance(player, 20);

        scheduleTicks(10, () ->
                ProjectileUtils.launchCustom(player, 3.0F, skillProjectile ->
                        skillProjectile.setDisplayItem(ModItems.PEGASUS_ELEMENT.get())
                                .setBaseDamage(2)
                                .setExplosionPower(4)
                                .setGravity(0)
                                .setLifeTime(100)
                                .onHitEntity((proj, target) -> {
                                    if (target instanceof LivingEntity living) {
                                        living.addEffect(new MobEffectInstance(
                                                MobEffects.MOVEMENT_SLOWDOWN, 100, 2
                                        ));
                                    }
                                })));
    }

    private static void risingCalamityTitan(Player player) {
        addResistance(player, 20);

        double distance = 2.0;
        scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                5));
    }

    private static void amazingMightyKick(Player player) {
        int duration = calculateTolerance(40);

        addResistance(player, duration);

        kickSequence(player, RiderSkillFlags.AMAZING_MIGHTY_KICK, duration);
    }

    private static void ultimateKick(Player player) {
        int duration = calculateTolerance(40);

        addResistance(player, duration);

        kickSequence(player, RiderSkillFlags.ULTIMATE_KICK, duration);
    }

    // 亚极陀
    private static void groundKick(Player player) {
        int duration = calculateTolerance(70);
        Level level = player.level();
        AgitoKickEffect effect = new AgitoKickEffect(ModEntities.AGITO_KICK_EFFECT.get(), level);
        effect.setOwner(player);
        level.addFreshEntity(effect);

        addResistance(player, duration);
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            scheduleTicks(duration, agitoGround::setClosed);
        }

        kickSequence(player, RiderSkillFlags.GROUND_KICK, duration);
    }

    private static void flameSaber(Player player) {
        ItemStack flameSaber = ModItems.FLAME_SABER.toStack();
        if (!player.getInventory().add(flameSaber)) player.drop(flameSaber, false);
    }

    private static void stormHalberd(Player player) {
        ItemStack stormHalberd = ModItems.STORM_HALBERD.toStack();
        if (!player.getInventory().add(stormHalberd)) player.drop(stormHalberd, false);
    }

    private static void trinityWeapon(Player player) {
        flameSaber(player);
        stormHalberd(player);
    }

    private static void shiningCalibur(Player player) {
        ItemStack shiningCalibur = ModItems.SHINING_CALIBUR.toStack();
        if (!player.getInventory().add(shiningCalibur)) player.drop(shiningCalibur, false);

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

    private static int calculateTolerance(int origin) {
        return origin + getTolerance();
    }

    private static int getTolerance() {
        return Config.SKILL_TOLERANCE_TIME.get() * 20;
    }

    public static void addResistance(Player player, int duration) {
        addEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 4);
    }

    public static void addEffect(Player player, Holder<MobEffect> effect, int duration, int level) {
        player.addEffect(new MobEffectInstance(effect, duration, level, true, false));
    }

    private static void createExplosion(Player player, double x, double y, double z, float radius) {
        SkillUtils.explode(player, player.level(), x, y, z, radius, radius * 4.0f);
    }

    private static void createExplosion(Player player, LivingEntity entity, float radius) {
        BlockPos pos = entity.getOnPos();
        createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), radius);
    }

    private static void createKickExplosion(Player player, LivingEntity entity, float radius) {
        BlockPos pos = entity.getOnPos();
        // 骑士踢中心偏高（击中躯干）
        SkillUtils.explode(player, player.level(),
                pos.getX(), pos.getY() + 1.5, pos.getZ(), radius, radius * 4.0f);
    }

    // Tag辅助
    private static void addTag(Player player, String tag) {
        if (!player.getTags().contains(tag)) {
            player.addTag(tag);
        }
    }

    private static void removeTag(Player player, String tag) {
        if (player.getTags().contains(tag)) {
            player.removeTag(tag);
        }
    }

    private static void flagKick(Player player, int ticks) {
        scheduleTicks(10, () -> StateFlagManager.apply(player, RiderSkillFlags.KICKING, ticks));
    }

    private static void kickSequence(Player player, ResourceLocation flag, int duration) {
        flagKick(player, duration);
        RiderSkillFlags.apply(player, flag, duration);
    }

    // 伤害辅助
    private static void hurt(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.hurt(player.damageSources().playerAttack(player), amount);
        }
    }

    private static void knockBack(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.knockback(amount, -player.getLookAngle().x, -player.getLookAngle().z);
        }
    }

    private static void scheduleTicks(int ticks, Runnable runnable) {
        RideBattleAPI.scheduleTicks(ticks, runnable);
    }

    private static void sendSkillEventToClient(Player player, ResourceLocation skillId) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new PWClientSkillPacket(skillId));
    }
}
