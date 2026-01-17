package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.core.system.skill.SkillSystem;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

public class RiderSkills {
    // 空我
    public static final ResourceLocation GROWING_KICK = fromString("growing_kick");
    public static final ResourceLocation MIGHTY_KICK = fromString("mighty_kick");
    public static final ResourceLocation MIGHTY_PUNCH = fromString("mighty_punch");
    public static final ResourceLocation SPLASH_DRAGON = fromString("splash_dragon");
    public static final ResourceLocation BLAST_PEGASUS = fromString("blast_pegasus");
    public static final ResourceLocation CALAMITY_TITAN = fromString("calamity_titan");
    public static final ResourceLocation RISING_MIGHTY_KICK = fromString("rising_mighty_kick");
    public static final ResourceLocation RISING_SPLASH_DRAGON = fromString("rising_splash_dragon");
    public static final ResourceLocation RISING_BLAST_PEGASUS = fromString("rising_blast_pegasus");
    public static final ResourceLocation RISING_CALAMITY_TITAN = fromString("rising_calamity_titan");
    public static final ResourceLocation AMAZING_MIGHTY_KICK = fromString("amazing_mighty_kick");
    public static final ResourceLocation ULTRA_KICK = fromString("ultra_kick");

    // 亚极陀
    public static final ResourceLocation GROUND_KICK = fromString("ground_kick");
    public static final ResourceLocation SABER_SLASH = fromString("saber_slash");

    private static void registerKuugaSkills(){
        registerSkill(GROWING_KICK, 10, ChatFormatting.WHITE);
        registerSkill(MIGHTY_KICK, 15, ChatFormatting.RED);
        registerSkill(MIGHTY_PUNCH, 15, ChatFormatting.RED);
        registerSkill(SPLASH_DRAGON, 15);
        registerSkill(BLAST_PEGASUS, 5);
        registerSkill(CALAMITY_TITAN, 15);
        registerSkill(RISING_MIGHTY_KICK, 20, ChatFormatting.GOLD);
        registerSkill(RISING_SPLASH_DRAGON, 20);
        registerSkill(RISING_BLAST_PEGASUS, 10);
        registerSkill(RISING_CALAMITY_TITAN, 20);
        registerSkill(AMAZING_MIGHTY_KICK, 25, ChatFormatting.BLACK);
        registerSkill(ULTRA_KICK, 30, ChatFormatting.BLACK);

        registerSkill(GROUND_KICK, 15, ChatFormatting.YELLOW);
        registerSkill(SABER_SLASH, 15);
    }

    private static void registerSkill(ResourceLocation id, int cooldown, ChatFormatting chatFormat){
        String name = id.toString().toLowerCase().replace(RideBattleParallelWorlds.MODID + ":", "");
        SkillSystem.registerSkill(id, Component.translatable("skill." + name).withStyle(chatFormat), cooldown);
    }

    private static void registerSkill(ResourceLocation id, int cooldown){
        registerSkill(id, cooldown, ChatFormatting.BOLD);
    }

    public static void init(){
        registerKuugaSkills();
    }
}
