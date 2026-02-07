package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.core.system.skill.SkillSystem;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

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
    public static final ResourceLocation FLAME_SABER = fromString("flame_saber");
    public static final ResourceLocation STORM_HALBERD = fromString("storm_halberd");
    public static final ResourceLocation TRINITY_WEAPON = fromString("trinity_weapon");
    public static final ResourceLocation SABER_SLASH = fromString("saber_slash");
    public static final ResourceLocation HALBERD_SPIN = fromString("halberd_spin");
    public static final ResourceLocation FIRESTORM_ATTACK = fromString("firestorm_attack");


    private static void registerKuugaSkills(){
        registerSkill(GROWING_KICK, 10, ChatFormatting.WHITE);
        registerSkill(MIGHTY_KICK, 15, ChatFormatting.RED);
        registerSkill(MIGHTY_PUNCH, 15, ChatFormatting.RED, true);
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
        registerSkill(FLAME_SABER, 15, ChatFormatting.RED);
        registerSkill(SABER_SLASH, 15, true);
        registerSkill(STORM_HALBERD, 15, ChatFormatting.BLUE);
        registerSkill(HALBERD_SPIN, 15, true);
        registerSkill(TRINITY_WEAPON, 15, ChatFormatting.GOLD);
        registerSkill(FIRESTORM_ATTACK, 20, true);
    }

    private static void registerSkill(ResourceLocation id, int cooldown, ChatFormatting chatFormat, boolean buffered){
        String name = id.toString().toLowerCase().replace(RideBattleParallelWorlds.MODID + ":", "");
        SkillSystem.registerSkill(id, Component.translatable("skill." + name).withStyle(chatFormat), cooldown);
        String tag = "skill_" + name;
        RideBattleParallelWorlds.LOGGER.debug(tag);
        if (buffered) BUFFERED_SKILL_TAGS.put(id, tag);
    }

    private static void registerSkill(ResourceLocation id, int cooldown, ChatFormatting chatFormat){
        registerSkill(id, cooldown, chatFormat, false);
    }

    private static void registerSkill(ResourceLocation id, int cooldown){
        registerSkill(id, cooldown, ChatFormatting.BOLD);
    }

    private static void registerSkill(ResourceLocation id, int cooldown, boolean buffered){
        registerSkill(id, cooldown, ChatFormatting.BOLD, buffered);
    }

    public static Map<ResourceLocation, String> BUFFERED_SKILL_TAGS = new HashMap<>();

    public static void init(){
        registerKuugaSkills();
    }
}
