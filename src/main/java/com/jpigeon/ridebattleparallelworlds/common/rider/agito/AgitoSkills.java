package com.jpigeon.ridebattleparallelworlds.common.rider.agito;

import com.jpigeon.ridebattleparallelworlds.common.registry.ModEntities;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkillFlags;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.entity.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Agito.*;

public class AgitoSkills {
    private AgitoSkills() {
    }

    public static void register() {
        PWSkillUtils.registerSkillMap(Map.ofEntries(
                Map.entry(RiderSkills.GROUND_KICK, AgitoSkills::groundKick),
                Map.entry(RiderSkills.FLAME_SABER, AgitoSkills::flameSaber),
                Map.entry(RiderSkills.STORM_HALBERD, AgitoSkills::stormHalberd),
                Map.entry(RiderSkills.TRINITY_WEAPON, AgitoSkills::trinityWeapon),
                Map.entry(RiderSkills.SHINING_CALIBUR, AgitoSkills::shiningCalibur)
        ));

        // 技能注册（冷却 + 颜色）
        RiderSkills.registerSkill(RiderSkills.GROUND_KICK, 15, ChatFormatting.YELLOW);
        RiderSkills.registerSkill(RiderSkills.FLAME_SABER, 15, ChatFormatting.RED);
        RiderSkills.registerSkill(RiderSkills.SABER_SLASH, 15);
        RiderSkills.registerSkill(RiderSkills.STORM_HALBERD, 15, ChatFormatting.BLUE);
        RiderSkills.registerSkill(RiderSkills.HALBERD_SPIN, 15);
        RiderSkills.registerSkill(RiderSkills.TRINITY_WEAPON, 15, ChatFormatting.GOLD);
        RiderSkills.registerSkill(RiderSkills.FIRESTORM_ATTACK, 20);
        RiderSkills.registerSkill(RiderSkills.SHINING_CALIBUR, 15);
        RiderSkills.registerSkill(RiderSkills.BURNING_BOMBER, 30);
    }

    private static void groundKick(Player player) {
        Level level = player.level();
        AgitoKickEffect effect = new AgitoKickEffect(ModEntities.AGITO_KICK_EFFECT.get(), level);
        effect.setOwner(player);
        level.addFreshEntity(effect);

        int duration = PWSkillUtils.performRiderKick(player, RiderSkillFlags.GROUND_KICK, 70);
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            scheduleTicks(duration, agitoGround::setClosed);
        }
    }

    private static void flameSaber(Player player) {
        ItemStack flameSaber = FLAME_SABER.toStack();
        if (!player.getInventory().add(flameSaber)) player.drop(flameSaber, false);
    }

    private static void stormHalberd(Player player) {
        ItemStack stormHalberd = STORM_HALBERD.toStack();
        if (!player.getInventory().add(stormHalberd)) player.drop(stormHalberd, false);
    }

    private static void trinityWeapon(Player player) {
        flameSaber(player);
        stormHalberd(player);
    }

    private static void shiningCalibur(Player player) {
        ItemStack shiningCalibur = SHINING_CALIBUR.toStack();
        if (!player.getInventory().add(shiningCalibur)) player.drop(shiningCalibur, false);

    }
}
