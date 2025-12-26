package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.handler.util.SkillUtils;
import com.jpigeon.ridebattleparallelworlds.core.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

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

    private static void handleSkill(Player serverPlayer, ResourceLocation skillId) {
        applyRiderSkills(serverPlayer, skillId);
    }

    private static void animateRiderSkills(Player player, ResourceLocation skillId) {
        if (KuugaConfig.KUUGA.includesFormId(RiderManager.getCurrentFormId(player))) {
            animateKuugaSkills(player, skillId);
        }
    }

    private static void applyRiderSkills(Player player, ResourceLocation skillId) {
        if (KuugaConfig.KUUGA.includesFormId(RiderManager.getCurrentFormId(player))) {
            applyKuugaSkills(player, skillId);
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
        RiderManager.scheduleTicks(30, () -> createKickExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 100));
    }

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
            if (mainHand.getItem() instanceof DragonRodItem dragonRod) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_main", 0);
                dragonRod.triggerMainSpin();
                RiderManager.scheduleTicks(15, () -> dragonRod.setCurrentState(DragonRodItem.AnimState.IDLE));
            } else if (offHand.getItem() instanceof DragonRodItem dragonRod) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_off", 0);
                dragonRod.triggerOffSpin();
                RiderManager.scheduleTicks(15, () -> dragonRod.setCurrentState(DragonRodItem.AnimState.IDLE));
            }
        } else if (skillId.equals(RiderSkills.BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof PegasusBowgunItem pegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_main");
                pegasusBowgunItem.triggerShoot();
            } else if (offHand.getItem() instanceof PegasusBowgunItem pegasusBowgunItem) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_off");
                pegasusBowgunItem.triggerShoot();
            }
        } else if (skillId.equals(RiderSkills.CALAMITY_TITAN)) {
            playAnimation(clientPlayer, "kuuga_calamity_titan");
            if (mainHand.getItem() instanceof TitanSwordItem titanSwordItem) {
                titanSwordItem.setCurrentState(TitanSwordItem.AnimState.STAB);
                RiderManager.scheduleTicks(15, () -> titanSwordItem.setCurrentState(TitanSwordItem.AnimState.IDLE));
            } else if (offHand.getItem() instanceof TitanSwordItem titanSwordItem) {
                titanSwordItem.setCurrentState(TitanSwordItem.AnimState.STAB);
                RiderManager.scheduleTicks(15, () -> titanSwordItem.setCurrentState(TitanSwordItem.AnimState.IDLE));
            }

        } else if (skillId.equals(RiderSkills.RISING_SPLASH_DRAGON)) {
            if (mainHand.getItem() instanceof RisingDragonRodItem risingDragonRod) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_main", 0);
                risingDragonRod.triggerMainSpin();
                RiderManager.scheduleTicks(15, () -> risingDragonRod.setCurrentState(RisingDragonRodItem.AnimState.IDLE));
            } else if (offHand.getItem() instanceof RisingDragonRodItem risingDragonRod) {
                playAnimation(clientPlayer, "kuuga_splash_dragon_off", 0);
                risingDragonRod.triggerOffSpin();
                RiderManager.scheduleTicks(15, () -> risingDragonRod.setCurrentState(RisingDragonRodItem.AnimState.IDLE));
            }

        } else if (skillId.equals(RiderSkills.RISING_BLAST_PEGASUS)) {
            if (mainHand.getItem() instanceof RisingPegasusBowgunItem risingPegasusBowgun) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_main");
                risingPegasusBowgun.triggerShoot();
            } else if (offHand.getItem() instanceof RisingPegasusBowgunItem risingPegasusBowgun) {
                playAnimation(clientPlayer, "kuuga_blast_pegasus_off");
                risingPegasusBowgun.triggerShoot();
            }
        } else if (skillId.equals(RiderSkills.RISING_CALAMITY_TITAN)) {
            playAnimation(clientPlayer, "kuuga_calamity_titan");
            if (mainHand.getItem() instanceof RisingTitanSwordItem risingTitanSword) {
                risingTitanSword.triggerStab();
                RiderManager.scheduleTicks(15, () -> risingTitanSword.setCurrentState(RisingTitanSwordItem.AnimState.IDLE));
            } else if (offHand.getItem() instanceof RisingTitanSwordItem risingTitanSword) {
                risingTitanSword.triggerStab();
                RiderManager.scheduleTicks(15, () -> risingTitanSword.setCurrentState(RisingTitanSwordItem.AnimState.IDLE));
            }
        }
    }

    private static void applyKuugaSkills(Player player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROWING_KICK)) {
            growingKick(player);
        } else if (skillId.equals(RiderSkills.MIGHTY_KICK)) {
            mightyKick(player);
        } else if (skillId.equals(RiderSkills.SPLASH_DRAGON)) {
            splashDragon(player);
        } else if (skillId.equals(RiderSkills.BLAST_PEGASUS)) {
            blastPegasus(player);
        } else if (skillId.equals(RiderSkills.CALAMITY_TITAN)) {
            calamityTitan(player);
        } else if (skillId.equals(RiderSkills.RISING_MIGHTY_KICK)) {
            risingMightyKick(player);
        } else if (skillId.equals(RiderSkills.RISING_SPLASH_DRAGON)) {
            risingSplashDragon(player);
        } else if (skillId.equals(RiderSkills.RISING_BLAST_PEGASUS)) {
            risingBlastPegasus(player);
        } else if (skillId.equals(RiderSkills.RISING_CALAMITY_TITAN)) {
            risingCalamityTitan(player);
        } else if (skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK)) {
            amazingMightyKick(player);
        } else if (skillId.equals(RiderSkills.ULTRA_KICK)) {
            ultimateKick(player);
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
