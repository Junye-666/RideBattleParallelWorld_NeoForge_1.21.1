package com.jpigeon.ridebattleparallelworlds.core.handler;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
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

    private static void handleSkill(Player serverPlayer, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROWING_KICK)) {
            growingKick(serverPlayer);
        }
        if (skillId.equals(RiderSkills.MIGHTY_KICK)) {
            mightyKick(serverPlayer);
        }
        if (skillId.equals(RiderSkills.RISING_MIGHTY_KICK)) {
            risingMightyKick(serverPlayer);
        }
        if (skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK)) {
            amazingMightyKick(serverPlayer);
        }
        if (skillId.equals(RiderSkills.ULTRA_KICK)) {
            ultimateKick(serverPlayer);
        }
    }

    private static void growingKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 30);
        animateKuugaKick(localPlayer);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 1));
        RiderManager.scheduleTicks(20, () -> createExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 2));
    }

    private static void mightyKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 30);
        animateKuugaKick(localPlayer);

        riderKickJump(localPlayer, 1);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 1.5));
        RiderManager.scheduleTicks(20, () -> createExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 3));
    }

    private static void risingMightyKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);
        animateKuugaKick(localPlayer);

        riderKickJump(localPlayer, 1.5);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2));
        RiderManager.scheduleTicks(25, () -> createExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 4));
    }

    private static void amazingMightyKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);
        animateKuugaKick(localPlayer);

        riderKickJump(localPlayer, 1.5);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2.5));
        RiderManager.scheduleTicks(25, () -> createExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 5));
    }

    private static void ultimateKick(Player serverPlayer) {
        LocalPlayer localPlayer = getLocalPlayer(serverPlayer);
        if (localPlayer == null) return;
        addResistance(serverPlayer, 40);
        animateKuugaKick(localPlayer);

        riderKickJump(localPlayer, 2);
        RiderManager.scheduleTicks(10, () -> riderKickForward(localPlayer, 2.5));
        RiderManager.scheduleTicks(30, () -> createExplosion(serverPlayer, serverPlayer.getBlockPosBelowThatAffectsMyMovement(), 6));
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
        player.addEffect(new MobEffectInstance(effect, duration, level, true, false ));
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

    private static void createExplosion(Player player, BlockPos pos, float damage) {
        Level level = player.level();

        // 创建爆炸
        level.explode(
                player,                              // 爆炸源
                pos.getX(),                          // X坐标
                pos.getY() + 1.5,                    // Y坐标
                pos.getZ(),                          // Z坐标
                damage,                              // 爆炸威力
                false,
                Config.SKILL_EXPLODE_GRIEF.get() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE     // 爆炸类型
        );
    }

    private static void playAnimation(AbstractClientPlayer player, String animationId, int fadeDuration) {
        PlayerAnimationTrigger.playAnimation(player, animationId, fadeDuration);
    }

    private static void animateKuugaKick(AbstractClientPlayer player) {
        playAnimation(player, "kuuga_mighty_kick", 0);
        RiderManager.scheduleTicks(33, () -> playAnimation(player, "player_reset", 5));
    }
}
