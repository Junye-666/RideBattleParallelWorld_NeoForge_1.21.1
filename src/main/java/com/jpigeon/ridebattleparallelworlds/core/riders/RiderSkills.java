package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.core.system.skill.SkillSystem;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RiderSkills {
    public static final ResourceLocation GROWING_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "growing_kick");
    public static final ResourceLocation MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mighty_kick");

    private static void registerKuugaSkills(){
        SkillSystem.registerSkillName(RiderSkills.GROWING_KICK, Component.translatable("skill.growing_kick"));
        SkillSystem.registerSkillName(RiderSkills.MIGHTY_KICK, Component.translatable("skill.mighty_kick"));

    }

    public static void init(){
        registerKuugaSkills();
    }
}
