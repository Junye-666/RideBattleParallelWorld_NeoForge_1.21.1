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
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.item.*;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.HashMap;
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
        SKILL_MAP.put(RiderSkills.SABER_SLASH, SkillHandler::saberSlash);
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
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 30);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 1));
        RiderManager.scheduleTicks(20, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 2));
    }

    private static void mightyKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 30);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 1.5));
        RiderManager.scheduleTicks(20, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 3));
    }

    private static void mightyPunch(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;

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
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2));
        RiderManager.scheduleTicks(25, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 4));
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
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2.5));
        RiderManager.scheduleTicks(25, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 5));
    }

    private static void ultimateKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);

        riderKickJump(localPlayer, 1.5);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2.5));
        RiderManager.scheduleTicks(30, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 7));
    }

    private static void groundKick(Player serverPlayer) {
        Level level = serverPlayer.level();
        AgitoKickEffect effect = new AgitoKickEffect(ModEntities.AGITO_KICK_EFFECT.get(), level);
        effect.setOwner(serverPlayer);
        level.addFreshEntity(effect);

        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 170);
        if (serverPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            RiderManager.scheduleTicks(160, agitoGround::setClosed);
        }

        RiderManager.scheduleTicks(130, () -> riderKickJump(localPlayer, 1.5));
        RiderManager.scheduleTicks(150, () -> riderKickForward(localPlayer, 1.5));
        RiderManager.scheduleTicks(160, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 3));
    }

    private static void saberSlash(Player player) {
        // TODO: 添加延迟伤害
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
            if (mainHand.getItem() instanceof RisingPegasusBowgunItem ) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_main");
            } else if (offHand.getItem() instanceof RisingPegasusBowgunItem ) {
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
            RiderManager.scheduleTicks(130, () -> playAnimation(clientPlayer, "agito_kick", 5));
        }

        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (skillId.equals(RiderSkills.SABER_SLASH)) {
            if (mainHand.getItem() instanceof FlameSaberItem flameSaber) {
                flameSaber.triggerOpen();
            } else if (offHand.getItem() instanceof FlameSaberItem flameSaber) {
                flameSaber.triggerOpen();
            }
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
        player.addDeltaMovement(new Vec3(0, jumpHeight, 0));
        player.hurtMarked = true;
    }

    private static void riderKickForward(Player player, double norm) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 movement = player.getDeltaMovement();
        player.addDeltaMovement(new Vec3(
                movement.x + lookVec.x * norm * 1.5,
                movement.y + lookVec.y * norm,
                movement.z + lookVec.z * norm * 1.5
        ));
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

    private static void createKickExplosion(Player player, BlockPos pos, float damage) {
        createExplosion(player, pos.getX(), pos.getY() + 1.5, pos.getZ(), damage);
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId, int fadeDuration) {
        PlayerAnimationTrigger.playAnimation(player, animationId, fadeDuration);
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId) {
        playAnimation(player, animationId, 0);
    }
}
