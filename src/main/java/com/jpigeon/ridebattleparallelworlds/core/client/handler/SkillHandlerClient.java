package com.jpigeon.ridebattleparallelworlds.core.client.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.util.ScheduleUtils;
import com.jpigeon.ridebattleparallelworlds.core.client.ClientUtils;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.PegasusBowgunItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.RisingDragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.RisingPegasusBowgunItem;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SkillHandlerClient {
    private static final Map<ResourceLocation, Consumer<Player>> SKILL_CLIENT_MAP = new HashMap<>();

    public static void registerSkillMap() {
        SKILL_CLIENT_MAP.put(RiderSkills.GROWING_KICK, SkillHandlerClient::growingKick);
        SKILL_CLIENT_MAP.put(RiderSkills.MIGHTY_KICK, SkillHandlerClient::mightyKick);
        SKILL_CLIENT_MAP.put(RiderSkills.RISING_MIGHTY_KICK, SkillHandlerClient::risingMightyKick);
        SKILL_CLIENT_MAP.put(RiderSkills.AMAZING_MIGHTY_KICK, SkillHandlerClient::amazingMightyKick);
        SKILL_CLIENT_MAP.put(RiderSkills.ULTIMATE_KICK, SkillHandlerClient::ultimateKick);
        SKILL_CLIENT_MAP.put(RiderSkills.GROUND_KICK, SkillHandlerClient::groundKick);
    }

    public static void handleSkillClient(Player player, ItemStack driver, ResourceLocation skillId) {
        Consumer<Player> skillConsumer = SKILL_CLIENT_MAP.get(skillId);
        if (skillConsumer != null) {
            skillConsumer.accept(player);
        }
        animateRiderSkills(player, skillId);
    }

    // ==========技能移动逻辑==========
    // 空我
    private static void growingKick(Player player) {
        riderKickJump(player, 1);
        riderKickForward(player, 1, 10);
    }

    private static void mightyKick(Player player) {
        riderKickJump(player, 1.1);
        riderKickForward(player, 1.5, 10);
    }

    private static void risingMightyKick(Player player) {
        riderKickJump(player, 1.2);
        riderKickForward(player, 2, 10);
    }

    private static void amazingMightyKick(Player player) {
        riderKickJump(player, 1.4);
        riderKickForward(player, 2.5, 15);
    }

    private static void ultimateKick(Player player) {
        riderKickJump(player, 1.5);
        riderKickForward(player, 2.5, 15);
    }

    // 亚极陀
    private static void groundKick(Player player) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            scheduleTicks(70, agitoGround::setClosed);
        }

        riderKickJump(player, 1.3, 30);
        riderKickForward(player, 1.8, 45);
    }

    // ==========动画逻辑==========
    private static void animateRiderSkills(Player player, ResourceLocation skillId) {
        if (KuugaConfig.KUUGA.includesFormId(RideBattleAPI.getCurrentFormId(player))) {
            animateKuugaSkills(player, skillId);
        } else if (AgitoConfig.AGITO.includesFormId(RideBattleAPI.getCurrentFormId(player))) {
            animateAgitoSkills(player, skillId);
        }
    }

    private static void animateKuugaSkills(Player player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROWING_KICK) || skillId.equals(RiderSkills.MIGHTY_KICK) || skillId.equals(RiderSkills.RISING_MIGHTY_KICK) || skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK) || skillId.equals(RiderSkills.ULTIMATE_KICK)) {
            playAnimation(player, "kuuga_mighty_kick", 0);
            scheduleTicks(33, () -> playAnimation(player, "player_reset", 5));
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
            scheduleTicks(35, () -> playAnimation(player, "agito_kick", 2));
        }
    }

    private static void playAnimation(Player player, String animationId, int fadeDuration) {
        PlayerAnimationHandler.handleAnimation(player, animationId, fadeDuration);
    }

    private static void playAnimation(Player player, String animationId) {
        PlayerAnimationHandler.handleAnimation(player, animationId, 0);
    }

    // 骑士踢逻辑辅助
    private static void riderKickJump(Player player, double jumpHeight, int ticks) {
        if (player == null) return;
        Vec3 currentMovement = player.getDeltaMovement();
        Vec3 jump = new Vec3(currentMovement.x, currentMovement.y + jumpHeight, currentMovement.z);
        scheduleTicks(ticks, () -> addDeltaMovement(player, jump));
    }

    private static void riderKickJump(Player player, double jumpHeight) {
        riderKickJump(player, jumpHeight, 0);
    }

    private static void riderKickForward(Player player, double norm, int ticks) {
        if (player == null) return;
        Vec3 lookVec = player.getLookAngle();
        Vec3 movement = player.getDeltaMovement();
        Vec3 kick = new Vec3(
                movement.x + lookVec.x * norm * 1.5,
                movement.y + lookVec.y * norm,
                movement.z + lookVec.z * norm * 1.5
        );
        scheduleTicks(ticks, () -> addDeltaMovement(player, kick));
    }

    private static void riderKickForward(Player player, double norm) {
        riderKickForward(player, norm, 0);
    }

    // 玩家移动辅助
    private static void addDeltaMovement(Player player, double x, double y, double z) {
        if (player instanceof LocalPlayer localPlayer) {
            ClientUtils.deplacePlayer(localPlayer, x, y, z, "add");
        }
    }

    private static void addDeltaMovement(Player player, Vec3 movement) {
        addDeltaMovement(player, movement.x(), movement.y(), movement.z());
    }

    private static void setDeltaMovement(Player player, double x, double y, double z) {
        if (player instanceof LocalPlayer localPlayer) {
            ClientUtils.deplacePlayer(localPlayer, x, y, z, "set");
        }
    }

    private static void setDeltaMovement(Player player, Vec3 movement) {
        setDeltaMovement(player, movement.x(), movement.y(), movement.z());
    }

    private static void kickRecoil(Player player) {
        Vec3 angle = player.getLookAngle();
        Vec3 current = player.getKnownMovement();
        Vec3 back = new Vec3(-(angle.x * current.x), 0.5, -(angle.z * current.z));
        setDeltaMovement(player, 0, 0, 0);
        addDeltaMovement(player, back);
    }

    private static void kickRecoilIn(Player player, int ticks) {
        ScheduleUtils.getInstance().scheduleTask(ticks, () -> kickRecoil(player));
    }

    private static void scheduleTicks(int ticks, Runnable runnable) {
        RideBattleAPI.scheduleTicks(ticks, runnable);
    }
}
