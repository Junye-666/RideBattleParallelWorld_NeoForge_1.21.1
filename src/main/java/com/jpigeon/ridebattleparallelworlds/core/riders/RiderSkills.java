package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.core.system.skill.SkillSystem;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RiderSkills {
    public static final ResourceLocation GROWING_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "growing_kick");
    public static final ResourceLocation MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mighty_kick");
    public static final ResourceLocation RISING_MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "rising_mighty_kick");
    public static final ResourceLocation AMAZING_MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "amazing_mighty_kick");
    public static final ResourceLocation ULTRA_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "ultra_kick");

    private static void registerKuugaSkills(){
        registerSkill(RiderSkills.GROWING_KICK, 10);
        registerSkill(RiderSkills.MIGHTY_KICK, 15);
        registerSkill(RiderSkills.RISING_MIGHTY_KICK, 15);
        registerSkill(RiderSkills.AMAZING_MIGHTY_KICK, 20);
        registerSkill(RiderSkills.ULTRA_KICK, 20);
    }

    private static void registerSkill(ResourceLocation id, int cooldown){
        String name = id.toString().toLowerCase().replace(RideBattleParallelWorlds.MODID + ":", "");
        RideBattleParallelWorlds.LOGGER.debug(name);
        Component translateKey = Component.translatable("skill." + name);
        RideBattleParallelWorlds.LOGGER.debug(translateKey.getString());
        SkillSystem.registerSkill(id, Component.translatable("skill." + name), cooldown);
    }

    public static void init(){
        registerKuugaSkills();
    }
}
