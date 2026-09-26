package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki;

import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public final class MirrorSkills {
    private MirrorSkills() {
    }

    public static void register() {
        PWSkillUtils.registerSkillMap(Map.ofEntries(
                Map.entry(RiderSkills.RYUKI_SWORD_VENT, MirrorSkills::swordVent),
                Map.entry(RiderSkills.RYUKI_STRIKE_VENT, MirrorSkills::strikeVent),
                Map.entry(RiderSkills.RYUKI_GUARD_VENT, MirrorSkills::guardVent),
                Map.entry(RiderSkills.RYUKI_ADVENT, MirrorSkills::advent),
                Map.entry(RiderSkills.RYUKI_FINAL_VENT, MirrorSkills::finalVent)
        ));

        // 冷却（秒），颜色
        RiderSkills.registerSkill(RiderSkills.RYUKI_SWORD_VENT, 5);
        RiderSkills.registerSkill(RiderSkills.RYUKI_STRIKE_VENT, 5);
        RiderSkills.registerSkill(RiderSkills.RYUKI_GUARD_VENT, 5);
        RiderSkills.registerSkill(RiderSkills.RYUKI_ADVENT, 5);
        RiderSkills.registerSkill(RiderSkills.RYUKI_FINAL_VENT, 10);
    }

    // 具体技能效果由你后续填充
    private static void swordVent(Player player) {

    }

    private static void strikeVent(Player player) {

    }

    private static void guardVent(Player player) {

    }

    private static void advent(Player player) {

    }

    private static void finalVent(Player player) {

    }
}
