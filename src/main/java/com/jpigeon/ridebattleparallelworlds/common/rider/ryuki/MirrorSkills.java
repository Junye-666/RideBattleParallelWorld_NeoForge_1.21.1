package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki;

import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import com.jpigeon.rideevolutionlib.util.item.TempItemUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

import static com.jpigeon.ridebattleparallelworlds.common.registry.ModItems.Ryuki.RYUKI_DRAG_SABER;

public final class MirrorSkills {
    private MirrorSkills() {
    }

    public static void register() {
        PWSkillUtils.registerSkillMap(Map.ofEntries(
                Map.entry(RiderSkills.RYUKI_SWORD_VENT, MirrorSkills::swordVentRyuki),
                Map.entry(RiderSkills.RYUKI_STRIKE_VENT, MirrorSkills::strikeVentRyuki),
                Map.entry(RiderSkills.RYUKI_GUARD_VENT, MirrorSkills::guardVentRyuki),
                Map.entry(RiderSkills.RYUKI_ADVENT, MirrorSkills::adventRyuki),
                Map.entry(RiderSkills.RYUKI_FINAL_VENT, MirrorSkills::finalVentRyuki)
        ));

        // 技能注册
        registerVent(RiderSkills.RYUKI_SWORD_VENT);
        registerVent(RiderSkills.RYUKI_STRIKE_VENT);
        registerVent(RiderSkills.RYUKI_GUARD_VENT);
        registerVent(RiderSkills.RYUKI_ADVENT);
        registerVent(RiderSkills.RYUKI_FINAL_VENT);
    }

    private static void registerVent(ResourceLocation skillId) {
        RiderSkills.registerSkill(skillId, 0);
    }

    // TODO
    private static void swordVentRyuki(Player player) {
        TempItemUtils.give(player, RYUKI_DRAG_SABER.get(), TempItemUtils.Scope.FORM);
    }

    private static void strikeVentRyuki(Player player) {

    }

    private static void guardVentRyuki(Player player) {

    }

    private static void adventRyuki(Player player) {

    }

    private static void finalVentRyuki(Player player) {

    }
}
