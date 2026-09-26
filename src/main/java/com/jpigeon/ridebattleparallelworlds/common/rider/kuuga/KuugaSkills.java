package com.jpigeon.ridebattleparallelworlds.common.rider.kuuga;

import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkillFlags;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.kuuga.item.RisingDragonRodItem;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import com.jpigeon.ridebattleparallelworlds.server.util.ProjectileUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Kuuga.PEGASUS_ELEMENT;
import static com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils.*;

public class KuugaSkills {
    private KuugaSkills() {
    }

    public static void register() {
        PWSkillUtils.registerSkillMap(Map.ofEntries(
                Map.entry(RiderSkills.GROWING_KICK, KuugaSkills::growingKick),
                Map.entry(RiderSkills.MIGHTY_KICK, KuugaSkills::mightyKick),
                Map.entry(RiderSkills.SPLASH_DRAGON, KuugaSkills::splashDragon),
                Map.entry(RiderSkills.BLAST_PEGASUS, KuugaSkills::blastPegasus),
                Map.entry(RiderSkills.CALAMITY_TITAN, KuugaSkills::calamityTitan),
                Map.entry(RiderSkills.RISING_MIGHTY_KICK, KuugaSkills::risingMightyKick),
                Map.entry(RiderSkills.RISING_SPLASH_DRAGON, KuugaSkills::risingSplashDragon),
                Map.entry(RiderSkills.RISING_BLAST_PEGASUS, KuugaSkills::risingBlastPegasus),
                Map.entry(RiderSkills.RISING_CALAMITY_TITAN, KuugaSkills::risingCalamityTitan),
                Map.entry(RiderSkills.AMAZING_MIGHTY_KICK, KuugaSkills::amazingMightyKick),
                Map.entry(RiderSkills.ULTIMATE_KICK, KuugaSkills::ultimateKick)
        ));

        // 技能注册（冷却 + 颜色）
        RiderSkills.registerSkill(RiderSkills.GROWING_KICK, 10, ChatFormatting.WHITE);
        RiderSkills.registerSkill(RiderSkills.MIGHTY_KICK, 15, ChatFormatting.RED);
        RiderSkills.registerSkill(RiderSkills.MIGHTY_PUNCH, 15, ChatFormatting.RED);
        RiderSkills.registerSkill(RiderSkills.SPLASH_DRAGON, 15);
        RiderSkills.registerSkill(RiderSkills.BLAST_PEGASUS, 5);
        RiderSkills.registerSkill(RiderSkills.CALAMITY_TITAN, 15);
        RiderSkills.registerSkill(RiderSkills.RISING_MIGHTY_KICK, 20, ChatFormatting.GOLD);
        RiderSkills.registerSkill(RiderSkills.RISING_SPLASH_DRAGON, 20);
        RiderSkills.registerSkill(RiderSkills.RISING_BLAST_PEGASUS, 10);
        RiderSkills.registerSkill(RiderSkills.RISING_CALAMITY_TITAN, 20);
        RiderSkills.registerSkill(RiderSkills.AMAZING_MIGHTY_KICK, 25, ChatFormatting.BLACK);
        RiderSkills.registerSkill(RiderSkills.ULTIMATE_KICK, 30, ChatFormatting.BLACK);
    }

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
                        skillProjectile.setDisplayItem(PEGASUS_ELEMENT.get())
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
                        skillProjectile.setDisplayItem(PEGASUS_ELEMENT.get())
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
}
