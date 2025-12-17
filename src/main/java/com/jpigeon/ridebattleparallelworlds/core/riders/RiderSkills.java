package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.core.system.skill.SkillSystem;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RiderSkills {
    public static final ResourceLocation GROWING_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "growing_kick");
    public static final ResourceLocation MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "mighty_kick");
    public static final ResourceLocation SPLASH_DRAGON = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "splash_dragon");
    public static final ResourceLocation RISING_MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "rising_mighty_kick");
    public static final ResourceLocation AMAZING_MIGHTY_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "amazing_mighty_kick");
    public static final ResourceLocation ULTRA_KICK = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, "ultra_kick");

    private static void registerKuugaSkills(){
        registerSkill(RiderSkills.GROWING_KICK, 10, ChatFormatting.WHITE);
        registerSkill(RiderSkills.MIGHTY_KICK, 15, ChatFormatting.RED);
        registerSkill(RiderSkills.SPLASH_DRAGON, 15, ChatFormatting.BLUE);
        registerSkill(RiderSkills.RISING_MIGHTY_KICK, 15, ChatFormatting.GOLD);
        registerSkill(RiderSkills.AMAZING_MIGHTY_KICK, 20, ChatFormatting.BLACK);
        registerSkill(RiderSkills.ULTRA_KICK, 20, ChatFormatting.BLACK);
    }

    private static void registerSkill(ResourceLocation id, int cooldown, ChatFormatting chatFormat){
        String name = id.toString().toLowerCase().replace(RideBattleParallelWorlds.MODID + ":", "");
        SkillSystem.registerSkill(id, Component.translatable("skill." + name).withStyle(chatFormat), cooldown);
    }

    public static void init(){
        registerKuugaSkills();
    }
}
