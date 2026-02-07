package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.core.handler.util.SkillUtils;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.StormHalberdItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.PegasusBowgunItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.RisingDragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.RisingPegasusBowgunItem;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
    }

    @SubscribeEvent
    public static void postSkill(SkillEvent.Post event) {
        animateRiderSkills(event.getPlayer(), event.getSkillId());
    }

    @SubscribeEvent
    public static void onDamageEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity living)) return;
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        handleBufferedDamage(player, living, mainHand, offHand, event);
    }

    @SubscribeEvent
    public static void onCollision(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!RiderManager.isTransformed(player)) return;
        if (player.level().isClientSide()) return;
        handleKickCollide(player);
    }

    private static final Map<ResourceLocation, Consumer<Player>> SKILL_MAP = new HashMap<>();

    static {
        SKILL_MAP.put(RiderSkills.GROWING_KICK, SkillHandler::growingKick);
        SKILL_MAP.put(RiderSkills.MIGHTY_KICK, SkillHandler::mightyKick);
        SKILL_MAP.put(RiderSkills.MIGHTY_PUNCH, SkillHandler::mightyPunch);
        SKILL_MAP.put(RiderSkills.SPLASH_DRAGON, SkillHandler::splashDragon);
        SKILL_MAP.put(RiderSkills.BLAST_PEGASUS, SkillHandler::blastPegasus);
        SKILL_MAP.put(RiderSkills.CALAMITY_TITAN, SkillHandler::calamityTitan);
        SKILL_MAP.put(RiderSkills.RISING_MIGHTY_KICK, SkillHandler::risingMightyKick);
        SKILL_MAP.put(RiderSkills.RISING_SPLASH_DRAGON, SkillHandler::risingSplashDragon);
        SKILL_MAP.put(RiderSkills.RISING_BLAST_PEGASUS, SkillHandler::risingBlastPegasus);
        SKILL_MAP.put(RiderSkills.RISING_CALAMITY_TITAN, SkillHandler::risingCalamityTitan);
        SKILL_MAP.put(RiderSkills.AMAZING_MIGHTY_KICK, SkillHandler::amazingMightyKick);
        SKILL_MAP.put(RiderSkills.ULTRA_KICK, SkillHandler::ultimateKick);

        SKILL_MAP.put(RiderSkills.GROUND_KICK, SkillHandler::groundKick);
        SKILL_MAP.put(RiderSkills.FLAME_SABER, SkillHandler::flameSaber);
        SKILL_MAP.put(RiderSkills.SABER_SLASH, SkillHandler::saberSlash);
        SKILL_MAP.put(RiderSkills.STORM_HALBERD, SkillHandler::stormHalberd);
        SKILL_MAP.put(RiderSkills.HALBERD_SPIN, SkillHandler::halberdSpin);
        SKILL_MAP.put(RiderSkills.TRINITY_WEAPON, SkillHandler::trinityWeapon);
        SKILL_MAP.put(RiderSkills.FIRESTORM_ATTACK, SkillHandler::firestormAttack);
    }

    private static void handleSkill(Player serverPlayer, ResourceLocation skillId) {
        Consumer<Player> skillConsumer = SKILL_MAP.get(skillId);
        if (skillConsumer != null) {
            skillConsumer.accept(serverPlayer);
            serverPlayer.hurtMarked = true;
        }
    }

    private static void animateRiderSkills(Player player, ResourceLocation skillId) {
        if (KuugaConfig.KUUGA.includesFormId(RiderManager.getCurrentFormId(player))) {
            animateKuugaSkills(player, skillId);
        } else if (AgitoConfig.AGITO.includesFormId(RiderManager.getCurrentFormId(player))) {
            animateAgitoSkills(player, skillId);
        }
    }
    private static void growingKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_growing_kick");

        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40 + getTolerance());

        riderKickJump(localPlayer, 1.5);
        riderKickForward(localPlayer, 1, 5);
        RiderManager.scheduleTicks(40 + getTolerance(), () -> removeTag(serverPlayer, "skill_growing_kick"));
    }

    private static void mightyKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_mighty_kick");
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40 + getTolerance());

        riderKickJump(localPlayer, 1);
        riderKickForward(localPlayer, 1.5, 10);
        RiderManager.scheduleTicks(40 + getTolerance(), () -> removeTag(serverPlayer, "skill_mighty_kick"));
    }

    private static void mightyPunch(Player serverPlayer) {
        addTag(serverPlayer, "skill_mighty_punch");
    }

    private static void splashDragon(Player serverPlayer) {
        addResistance(serverPlayer, 30);
        double distance;

        ItemStack mainHand = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = serverPlayer.getItemInHand(InteractionHand.OFF_HAND);
        if (mainHand.getItem() instanceof DragonRodItem) {
            distance = 2.0;
        } else if (offHand.getItem() instanceof DragonRodItem) {
            distance = 1.5;
        } else {
            distance = 0;
        }
        RiderManager.scheduleTicks(10, () -> createExplosion(serverPlayer,
                serverPlayer.getX() + serverPlayer.getLookAngle().x * distance,
                serverPlayer.getY() + 1.5 + serverPlayer.getLookAngle().y * distance,
                serverPlayer.getZ() + serverPlayer.getLookAngle().z * distance,
                3));
    }

    private static void blastPegasus(Player serverPlayer) {
        addResistance(serverPlayer, 20);

        RiderManager.scheduleTicks(10, () ->
                SkillUtils.launchCustom(serverPlayer, 3.0F, skillProjectile ->
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

    private static void calamityTitan(Player serverPlayer) {
        addResistance(serverPlayer, 20);

        double distance = 1.5;
        RiderManager.scheduleTicks(10, () -> createExplosion(serverPlayer,
                serverPlayer.getX() + serverPlayer.getLookAngle().x * distance,
                serverPlayer.getY() + 1 + serverPlayer.getLookAngle().y * distance,
                serverPlayer.getZ() + serverPlayer.getLookAngle().z * distance,
                3));
    }

    private static void risingMightyKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_rising_mighty_kick");
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40 + getTolerance());

        riderKickJump(localPlayer, 1);
        riderKickForward(localPlayer, 2, 10);
        RiderManager.scheduleTicks(40 + getTolerance(), () -> removeTag(serverPlayer, "skill_rising_mighty_kick"));
    }

    private static void risingSplashDragon(Player serverPlayer) {
        addResistance(serverPlayer, 30);
        double distance;

        ItemStack mainHand = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = serverPlayer.getItemInHand(InteractionHand.OFF_HAND);
        if (mainHand.getItem() instanceof RisingDragonRodItem) {
            distance = 2.5;
        } else if (offHand.getItem() instanceof RisingDragonRodItem) {
            distance = 2.0;
        } else {
            distance = 0;
        }
        RiderManager.scheduleTicks(10, () -> createExplosion(serverPlayer,
                serverPlayer.getX() + serverPlayer.getLookAngle().x * distance,
                serverPlayer.getY() + 1.5 + serverPlayer.getLookAngle().y * distance,
                serverPlayer.getZ() + serverPlayer.getLookAngle().z * distance,
                4));
    }

    private static void risingBlastPegasus(Player serverPlayer) {
        addResistance(serverPlayer, 30);

        addResistance(serverPlayer, 20);

        RiderManager.scheduleTicks(10, () ->
                SkillUtils.launchCustom(serverPlayer, 3.0F, skillProjectile ->
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

    private static void risingCalamityTitan(Player serverPlayer) {
        addResistance(serverPlayer, 20);

        double distance = 2.0;
        RiderManager.scheduleTicks(10, () -> createExplosion(serverPlayer,
                serverPlayer.getX() + serverPlayer.getLookAngle().x * distance,
                serverPlayer.getY() + 1 + serverPlayer.getLookAngle().y * distance,
                serverPlayer.getZ() + serverPlayer.getLookAngle().z * distance,
                5));
    }

    private static void amazingMightyKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_amazing_mighty_kick");
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40 + getTolerance());

        riderKickJump(localPlayer, 1);
        riderKickForward(localPlayer, 2.5, 10);
        RiderManager.scheduleTicks(40 + getTolerance(), () -> removeTag(serverPlayer, "skill_amazing_mighty_kick"));
    }

    private static void ultimateKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_ultimate_kick");
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40 + getTolerance());

        riderKickJump(localPlayer, 1.5);
        riderKickForward(localPlayer, 2.5, 10);
        RiderManager.scheduleTicks(40 + getTolerance(), () -> removeTag(serverPlayer, "skill_ultimate_kick"));
    }

    private static void groundKick(Player serverPlayer) {
        addTag(serverPlayer, "skill_ground_kick");
        Level level = serverPlayer.level();
        AgitoKickEffect effect = new AgitoKickEffect(ModEntities.AGITO_KICK_EFFECT.get(), level);
        effect.setOwner(serverPlayer);
        level.addFreshEntity(effect);

        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 150 + getTolerance());
        if (serverPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            RiderManager.scheduleTicks(80 + getTolerance(), agitoGround::setClosed);
        }

        RiderManager.scheduleTicks(50, () -> riderKickJump(localPlayer, 1.5));
        riderKickForward(localPlayer, 1.5, 70);
        RiderManager.scheduleTicks(80 + getTolerance(), () -> removeTag(serverPlayer, "skill_ground_kick"));
    }

    private static void flameSaber(Player player) {
        ItemStack flameSaber = new ItemStack(ModItems.FLAME_SABER.get());
        if (!player.getInventory().add(flameSaber)) player.drop(flameSaber, false);
    }

    private static void saberSlash(Player serverPlayer) {
        addTag(serverPlayer, "skill_saber_slash");

    }

    private static void stormHalberd(Player serverPlayer) {
        ItemStack stormHalberd = new ItemStack(ModItems.STORM_HALBERD.get());
        if (!serverPlayer.getInventory().add(stormHalberd)) serverPlayer.drop(stormHalberd, false);
    }

    private static void halberdSpin(Player serverPlayer) {
        addTag(serverPlayer, "skill_halberd_spin");
    }

    private static void trinityWeapon(Player serverPlayer) {
        flameSaber(serverPlayer);
        stormHalberd(serverPlayer);
    }

    private static void firestormAttack(Player serverPlayer) {
        addTag(serverPlayer, "skill_firestorm_attack");
    }

    private static void handleBufferedDamage(Player player, LivingEntity living, ItemStack mainHand, ItemStack offHand, AttackEntityEvent event) {
        List<String> skillTags = player.getTags().stream()
                .filter(tag -> tag.startsWith("skill_"))
                .toList();

        for (String skillTag : skillTags) {
            switch (skillTag) {
                case "skill_saber_slash" -> {
                    if (mainHand.getItem() instanceof FlameSaberItem flameSaber) {
                        hurt(player, living, 30);
                        flameSaber.setClose();
                    }
                }
                case "skill_halberd_spin" -> {
                    if (mainHand.getItem() instanceof StormHalberdItem stormHalberd) {
                        hurt(player, living, 35);
                        stormHalberd.setClose();
                    }
                }
                case "skill_firestorm_attack" -> {
                    if (mainHand.getItem() instanceof FlameSaberItem flameSaber
                            && offHand.getItem() instanceof StormHalberdItem stormHalberd) {
                        hurt(player, living, 70);
                        flameSaber.setClose();
                        stormHalberd.setClose();
                    }
                }
                case "skill_mighty_punch" -> {
                    hurt(player, living, 15);
                    knockBack(player, living, 2);
                }
                default -> {
                }
            }
            removeTag(player, skillTag);

            RiderManager.playPublicSound(player, SoundEvents.PLAYER_ATTACK_CRIT);
        }
    }

    private static void handleKickCollide(Player player) {
        Level level = player.level();
        List<String> skillTags = player.getTags().stream()
                .filter(tag -> tag.startsWith("skill_") && tag.endsWith("_kick"))
                .toList();
        if (skillTags.isEmpty()) return;

        // 使用玩家的视线方向作为踢击方向
        Vec3 lookAngle = player.getLookAngle();
        Vec3 playerPos = player.position();

        // 计算踢击的起始点
        Vec3 kickStart = playerPos.add(0, 0, 0).add(lookAngle.scale(0.5));

        // 创建踢击的检测区域
        double range = 1.0;
        double radius = 0.5;

        // 查找踢击范围内的实体
        AABB kickBox = new AABB(
                kickStart.x - radius, kickStart.y - radius, kickStart.z - radius,
                kickStart.x + lookAngle.x * range + radius,
                kickStart.y + lookAngle.y * range + radius,
                kickStart.z + lookAngle.z * range + radius
        );

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                kickBox,
                e -> e != player && e.isAlive()
        );
        if (entities.isEmpty()) return;

        for (LivingEntity entity : entities) {
            if (entity.getBoundingBox().intersects(kickBox) || entity.getBoundingBox().intersects(player.getBoundingBox())) {
                for (String skillTag : skillTags) {
                    switch (skillTag) {
                        case "skill_growing_kick" -> createKickExplosion(player, entity, 2);
                        case "skill_mighty_kick" -> createKickExplosion(player, entity, 3);
                        case "skill_rising_mighty_kick" -> createKickExplosion(player, entity, 4);
                        case "skill_amazing_mighty_kick" -> createKickExplosion(player, entity, 5);
                        case "skill_ultimate_kick" -> createKickExplosion(player, entity, 7);
                        default -> {
                        }
                    }
                    removeTag(player, skillTag);
                }
            }
        }
    }

    private static int getTolerance() {
        return Config.SKILL_TOLERANCE_TIME.get() * 20;
    }

    // 动画逻辑方法
    private static void animateKuugaSkills(Player player, ResourceLocation skillId) {
        AbstractClientPlayer clientPlayer = getLocalPlayer(player);
        if (skillId.equals(RiderSkills.GROWING_KICK) || skillId.equals(RiderSkills.MIGHTY_KICK) || skillId.equals(RiderSkills.RISING_MIGHTY_KICK) || skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK) || skillId.equals(RiderSkills.ULTRA_KICK)) {
            playAnimation(clientPlayer, "kuuga_mighty_kick", 0);
            RiderManager.scheduleTicks(33, () -> playAnimation(clientPlayer, "player_reset", 5));
            return;
        }

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (skillId.equals(RiderSkills.SPLASH_DRAGON)) {
            if (mainHand.getItem() instanceof DragonRodItem) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_main", 0);
            } else if (offHand.getItem() instanceof DragonRodItem) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_off", 0);
            }
        } else if (skillId.equals(RiderSkills.BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof PegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_main");
            } else if (offHand.getItem() instanceof PegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_off");
            }
        } else if (skillId.equals(RiderSkills.CALAMITY_TITAN)) {
            playAnimation(clientPlayer, "kuuga_calamity_titan");
        } else if (skillId.equals(RiderSkills.RISING_SPLASH_DRAGON)) {
            if (mainHand.getItem() instanceof RisingDragonRodItem) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_main", 0);
            } else if (offHand.getItem() instanceof RisingDragonRodItem) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_off", 0);
            }
        } else if (skillId.equals(RiderSkills.RISING_BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof RisingPegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_main");
            } else if (offHand.getItem() instanceof RisingPegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_off");
            }
        } else if (skillId.equals(RiderSkills.RISING_CALAMITY_TITAN)) {
            playAnimation(clientPlayer, "kuuga_calamity_titan");
        }
    }

    private static void animateAgitoSkills(Player player, ResourceLocation skillId) {
        AbstractClientPlayer clientPlayer = getLocalPlayer(player);
        if (skillId.equals(RiderSkills.GROUND_KICK)) {
            playAnimation(clientPlayer, "agito_kick_prepare", 0);
            RiderManager.scheduleTicks(45, () -> playAnimation(clientPlayer, "agito_kick", 2));
        }
    }

    // ===辅助方法===
    private static LocalPlayer getLocalPlayer(Player player) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getUUID().equals(player.getUUID()) ? mc.player : null;
    }

    public static void addResistance(Player player, int duration) {
        addEffect(player, MobEffects.DAMAGE_RESISTANCE, duration, 4);
    }

    public static void addEffect(Player player, Holder<MobEffect> effect, int duration, int level) {
        player.addEffect(new MobEffectInstance(effect, duration, level, true, false));
    }

    // 技能逻辑
    private static void riderKickJump(Player player, double jumpHeight) {
        Vec3 currentMovement = player.getKnownMovement();
        player.setDeltaMovement(new Vec3(currentMovement.x, jumpHeight, currentMovement.z));
    }

    private static void riderKickForward(Player player, double norm, int ticks) {
        RiderManager.scheduleTicks(ticks, () -> {
                    Vec3 lookVec = player.getLookAngle();
                    Vec3 movement = player.getDeltaMovement();
                    player.addDeltaMovement(new Vec3(
                            movement.x + lookVec.x * norm * 1.5,
                            movement.y + lookVec.y * norm,
                            movement.z + lookVec.z * norm * 1.5
                    ));
                }
        );
    }

    private static void createExplosion(Player player, double x, double y, double z, float damage) {
        Level level = player.level();

        // 创建爆炸
        level.explode(
                player,                              // 爆炸源
                x,                                   // X坐标
                y,                                   // Y坐标
                z,                                   // Z坐标
                damage,                              // 爆炸威力
                false,
                Config.SKILL_EXPLODE_GRIEF.get() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE     // 爆炸类型
        );
    }

    private static void createKickExplosion(Player player, LivingEntity entity, float damage) {
        BlockPos pos = entity.getOnPos();
        createExplosion(player, pos.getX(), pos.getY() + 1.5, pos.getZ(), damage);

        LocalPlayer localPlayer = getLocalPlayer(player);
        if (localPlayer != null) {
            Vec3 angle = localPlayer.getLookAngle();
            Vec3 current = localPlayer.getKnownMovement();
            Vec3 back = new Vec3(-(angle.x * current.x) , 0.5, -(angle.z * current.z));
            localPlayer.setDeltaMovement(0, 0, 0);
            localPlayer.addDeltaMovement(back);
        }
    }

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

    private static void hurt(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.hurt(target.damageSources().mobAttack(player), amount);
        }
    }

    private static void knockBack(Player player, LivingEntity target, float amount) {
        if (!target.level().isClientSide() && target.isAlive()) {
            target.knockback(amount, -player.getLookAngle().x, -player.getLookAngle().z);
        }
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId, int fadeDuration) {
        PlayerAnimationTrigger.playAnimation(player, animationId, fadeDuration);
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId) {
        playAnimation(player, animationId, 0);
    }
}
