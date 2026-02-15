package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.core.handler.util.SkillUtils;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.network.PWPacketHandler;
import com.jpigeon.ridebattleparallelworlds.core.network.packet.PWAnimationPacket;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.item.StormHalberdItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.PegasusBowgunItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.RisingDragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.RisingPegasusBowgunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.ArrayList;
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
        handleDamageEntity(player, living);
    }

    @SubscribeEvent
    public static void onCollision(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!RiderManager.isTransformed(player)) return;
        if (player.level().isClientSide()) return;
        handleKickCollide(player);
    }

    private static final Map<ResourceLocation, Consumer<Player>> SKILL_MAP = new HashMap<>();
    private static final List<ResourceLocation> TAGGED_SKILLS = new ArrayList<>();

    static {
        SKILL_MAP.put(RiderSkills.GROWING_KICK, SkillHandler::growingKick);
        SKILL_MAP.put(RiderSkills.MIGHTY_KICK, SkillHandler::mightyKick);
        SKILL_MAP.put(RiderSkills.SPLASH_DRAGON, SkillHandler::splashDragon);
        SKILL_MAP.put(RiderSkills.BLAST_PEGASUS, SkillHandler::blastPegasus);
        SKILL_MAP.put(RiderSkills.CALAMITY_TITAN, SkillHandler::calamityTitan);
        SKILL_MAP.put(RiderSkills.RISING_MIGHTY_KICK, SkillHandler::risingMightyKick);
        SKILL_MAP.put(RiderSkills.RISING_SPLASH_DRAGON, SkillHandler::risingSplashDragon);
        SKILL_MAP.put(RiderSkills.RISING_BLAST_PEGASUS, SkillHandler::risingBlastPegasus);
        SKILL_MAP.put(RiderSkills.RISING_CALAMITY_TITAN, SkillHandler::risingCalamityTitan);
        SKILL_MAP.put(RiderSkills.AMAZING_MIGHTY_KICK, SkillHandler::amazingMightyKick);
        SKILL_MAP.put(RiderSkills.ULTIMATE_KICK, SkillHandler::ultimateKick);
        SKILL_MAP.put(RiderSkills.GROUND_KICK, SkillHandler::groundKick);
        SKILL_MAP.put(RiderSkills.FLAME_SABER, SkillHandler::flameSaber);
        SKILL_MAP.put(RiderSkills.STORM_HALBERD, SkillHandler::stormHalberd);
        SKILL_MAP.put(RiderSkills.TRINITY_WEAPON, SkillHandler::trinityWeapon);
        SKILL_MAP.put(RiderSkills.SHINING_CALIBUR, SkillHandler::shiningCalibur);

        TAGGED_SKILLS.add(RiderSkills.MIGHTY_PUNCH);
        TAGGED_SKILLS.add(RiderSkills.SABER_SLASH);
        TAGGED_SKILLS.add(RiderSkills.HALBERD_SPIN);
        TAGGED_SKILLS.add(RiderSkills.FIRESTORM_ATTACK);
        TAGGED_SKILLS.add(RiderSkills.BURNING_BOMBER);
    }

    private static void handleSkill(Player serverPlayer, ResourceLocation skillId) {
        String tag = RiderSkills.SKILL_TAGS.get(skillId);
        if (tag != null) {
            addTag(serverPlayer, tag);
        }

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

    private static void growingKick(Player player) {
        int duration = calculateTolerance(40);
        kickSequence(player, duration);

        addResistance(player, duration);

        riderKickJump(player, 1.5);
        riderKickForward(player, 1, 5);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_growing_kick"));
    }

    private static void mightyKick(Player player) {
        int duration = calculateTolerance(40);
        kickSequence(player, duration);

        addResistance(player, duration);

        riderKickJump(player, 1.5);
        riderKickForward(player, 1.5, 10);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_mighty_kick"));
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
        RiderManager.scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1.5 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                3));
    }

    private static void blastPegasus(Player player) {
        addResistance(player, 20);

        RiderManager.scheduleTicks(10, () ->
                SkillUtils.launchCustom(player, 3.0F, skillProjectile ->
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
        RiderManager.scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                3));
    }

    private static void risingMightyKick(Player player) {
        int duration = calculateTolerance(40);
        kickSequence(player, duration);
        addTag(player, "skill_rising_mighty_kick");
        addResistance(player, duration);

        riderKickJump(player, 1.5);
        riderKickForward(player, 2, 10);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_rising_mighty_kick"));
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
        RiderManager.scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1.5 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                4));
    }

    private static void risingBlastPegasus(Player player) {
        addResistance(player, 30);

        addResistance(player, 20);

        RiderManager.scheduleTicks(10, () ->
                SkillUtils.launchCustom(player, 3.0F, skillProjectile ->
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
        RiderManager.scheduleTicks(10, () -> createExplosion(player,
                player.getX() + player.getLookAngle().x * distance,
                player.getY() + 1 + player.getLookAngle().y * distance,
                player.getZ() + player.getLookAngle().z * distance,
                5));
    }

    private static void amazingMightyKick(Player player) {
        int duration = calculateTolerance(40);
        kickSequence(player, duration);

        addResistance(player, duration);

        riderKickJump(player, 1.5);
        riderKickForward(player, 2.5, 10);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_amazing_mighty_kick"));
    }

    private static void ultimateKick(Player player) {
        int duration = calculateTolerance(40);
        kickSequence(player, duration);

        addResistance(player, duration);

        riderKickJump(player, 1.5);
        riderKickForward(player, 2.5, 10);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_ultimate_kick"));
    }

    private static void groundKick(Player player) {
        int duration = calculateTolerance(120);
        kickSequence(player, duration);
        Level level = player.level();
        AgitoKickEffect effect = new AgitoKickEffect(ModEntities.AGITO_KICK_EFFECT.get(), level);
        effect.setOwner(player);
        level.addFreshEntity(effect);

        addResistance(player, duration);
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            RiderManager.scheduleTicks(duration, agitoGround::setClosed);
        }

        RiderManager.scheduleTicks(50, () -> riderKickJump(player, 2));
        riderKickForward(player, 1.5, 70);
        RiderManager.scheduleTicks(duration, () -> removeTag(player, "skill_ground_kick"));
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
        if (shiningCalibur.getItem() instanceof ShiningCaliburItem calibur) {
            if (!player.getInventory().add(shiningCalibur)) player.drop(shiningCalibur, false);
            calibur.setClose();
        }
    }

    private static void handleDamageEntity(Player player, LivingEntity living) {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (RiderManager.isSpecificForm(player, AgitoConfig.BURNING_ID)) {
            living.igniteForSeconds(3);
        }

        handleBufferedDamage(player, living, mainHand, offHand);
    }

    private static void handleBufferedDamage(Player player, LivingEntity living, ItemStack mainHand, ItemStack offHand) {
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
                case "skill_burning_bomber" -> {
                    if (mainHand.getItem() instanceof ShiningCaliburItem) {
                        hurt(player, living, 60);
                        knockBack(player, living, 2);
                        RiderManager.scheduleTicks(20, () -> createExplosion(player, living, 4));
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
        if (!player.level().isClientSide && isKicking(player)) {

            List<String> skillTags = player.getTags().stream()
                    .filter(tag -> tag.startsWith("skill_") && tag.endsWith("_kick"))
                    .toList();
            if (skillTags.isEmpty()) return;

            Level level = player.level();

            // 水平方向（避免踢到天上）
            Vec3 look = player.getLookAngle();
            Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize();

            // 前方扩展距离（飞踢前伸）
            double forwardDistance = 0.8;

            // 扩展玩家碰撞盒
            AABB kickBox = player.getBoundingBox()
                    .expandTowards(horizontalLook.scale(forwardDistance))
                    .inflate(0.3); // 稍微加点宽度

            List<LivingEntity> entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    kickBox,
                    e -> e != player && e.isAlive()
            );

            if (entities.isEmpty()) return;

            for (LivingEntity entity : entities) {

                for (String skillTag : skillTags) {
                    switch (skillTag) {
                        case "skill_growing_kick" -> createKickExplosion(player, entity, 2);
                        case "skill_mighty_kick", "skill_ground_kick" -> createKickExplosion(player, entity, 3);
                        case "skill_rising_mighty_kick" -> createKickExplosion(player, entity, 4);
                        case "skill_amazing_mighty_kick" -> createKickExplosion(player, entity, 5);
                        case "skill_ultimate_kick" -> createKickExplosion(player, entity, 7);
                    }

                    removeTag(player, skillTag);
                    removeTag(player, "rider_kicking");
                }

                break;
            }
        }
    }

    private static boolean isKicking(Player player) {
        return player.getTags().contains("rider_kicking");
    }

    private static void kickSequence(Player player, int ticks) {
        RiderManager.scheduleTicks(10, () -> addTag(player, "rider_kicking"));
        RiderManager.scheduleTicks(ticks, () -> removeTag(player, "rider_kicking"));
    }

    private static int calculateTolerance(int origin) {
        return origin + getTolerance();
    }

    private static int getTolerance() {
        return Config.SKILL_TOLERANCE_TIME.get() * 20;
    }

    // 动画逻辑方法
    private static void animateKuugaSkills(Player player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROWING_KICK) || skillId.equals(RiderSkills.MIGHTY_KICK) || skillId.equals(RiderSkills.RISING_MIGHTY_KICK) || skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK) || skillId.equals(RiderSkills.ULTIMATE_KICK)) {
            playAnimation(player, "kuuga_mighty_kick", 0);
            RiderManager.scheduleTicks(33, () -> playAnimation(player, "player_reset", 5));
            return;
        }

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (skillId.equals(RiderSkills.SPLASH_DRAGON)) {
            if (mainHand.getItem() instanceof DragonRodItem) {
                playAnimation(player, "kuuga_splash_dragon_main");
            } else if (offHand.getItem() instanceof DragonRodItem) {
                playAnimation(player, "kuuga_splash_dragon_off");
            }
        } else if (skillId.equals(RiderSkills.BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof PegasusBowgunItem) {
                playAnimation(player, "kuuga_blast_pegasus_main");
            } else if (offHand.getItem() instanceof PegasusBowgunItem) {
                playAnimation(player, "kuuga_blast_pegasus_off");
            }
        } else if (skillId.equals(RiderSkills.CALAMITY_TITAN)) {
            playAnimation(player, "kuuga_calamity_titan");
        } else if (skillId.equals(RiderSkills.RISING_SPLASH_DRAGON)) {
            if (mainHand.getItem() instanceof RisingDragonRodItem) {
                playAnimation(player, "kuuga_splash_dragon_main");
            } else if (offHand.getItem() instanceof RisingDragonRodItem) {
                playAnimation(player, "kuuga_splash_dragon_off");
            }
        } else if (skillId.equals(RiderSkills.RISING_BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof RisingPegasusBowgunItem) {
                playAnimation(player, "kuuga_blast_pegasus_main");
            } else if (offHand.getItem() instanceof RisingPegasusBowgunItem) {
                playAnimation(player, "kuuga_blast_pegasus_off");
            }
        } else if (skillId.equals(RiderSkills.RISING_CALAMITY_TITAN)) {
            playAnimation(player, "kuuga_calamity_titan");
        }
    }

    private static void animateAgitoSkills(Player player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROUND_KICK)) {
            playAnimation(player, "agito_kick_prepare", 5);
            RiderManager.scheduleTicks(45, () -> playAnimation(player, "agito_kick", 2));
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
    private static void riderKickJump(Player serverPlayer, double jumpHeight) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        Vec3 currentMovement = localPlayer.getKnownMovement();
        localPlayer.setDeltaMovement(new Vec3(currentMovement.x, jumpHeight, currentMovement.z));

    }

    private static void riderKickForward(Player serverPlayer, double norm, int ticks) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        RiderManager.scheduleTicks(ticks, () -> {
                    Vec3 lookVec = localPlayer.getLookAngle();
                    Vec3 movement = localPlayer.getDeltaMovement();
                    localPlayer.addDeltaMovement(new Vec3(
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

    private static void createExplosion(Player player, LivingEntity entity, float damage) {
        BlockPos pos = entity.getOnPos();
        createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), damage);
    }

    private static void createKickExplosion(Player player, LivingEntity entity, float damage) {
        BlockPos pos = entity.getOnPos();
        createExplosion(player, pos.getX(), pos.getY() + 1.5, pos.getZ(), damage);

        LocalPlayer localPlayer = getLocalPlayer(player);
        if (localPlayer != null) {
            Vec3 angle = localPlayer.getLookAngle();
            Vec3 current = localPlayer.getKnownMovement();
            Vec3 back = new Vec3(-(angle.x * current.x), 0.5, -(angle.z * current.z));
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

    private static void playAnimation(Player player, String animationId, int fadeDuration) {
        if (player instanceof ServerPlayer serverPlayer) {
            PWPacketHandler.sendToClient(serverPlayer, new PWAnimationPacket(player.getUUID(), animationId, fadeDuration));
        }
    }

    private static void playAnimation(Player player, String animationId) {
        playAnimation(player, animationId, 0);
    }
}
