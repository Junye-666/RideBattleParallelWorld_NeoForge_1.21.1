package com.jpigeon.ridebattleparallelworlds.common.rider;

import com.jpigeon.ridebattlelib.server.system.SkillSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds.id;

public class RiderSkills {
    // 空我
    public static final ResourceLocation GROWING_KICK = id("growing_kick");
    public static final ResourceLocation MIGHTY_KICK = id("mighty_kick");
    public static final ResourceLocation MIGHTY_PUNCH = id("mighty_punch");
    public static final ResourceLocation SPLASH_DRAGON = id("splash_dragon");
    public static final ResourceLocation BLAST_PEGASUS = id("blast_pegasus");
    public static final ResourceLocation CALAMITY_TITAN = id("calamity_titan");
    public static final ResourceLocation RISING_MIGHTY_KICK = id("rising_mighty_kick");
    public static final ResourceLocation RISING_SPLASH_DRAGON = id("rising_splash_dragon");
    public static final ResourceLocation RISING_BLAST_PEGASUS = id("rising_blast_pegasus");
    public static final ResourceLocation RISING_CALAMITY_TITAN = id("rising_calamity_titan");
    public static final ResourceLocation AMAZING_MIGHTY_KICK = id("amazing_mighty_kick");
    public static final ResourceLocation ULTIMATE_KICK = id("ultimate_kick");

    // 亚极陀
    public static final ResourceLocation GROUND_KICK = id("ground_kick");
    public static final ResourceLocation FLAME_SABER = id("flame_saber");
    public static final ResourceLocation STORM_HALBERD = id("storm_halberd");
    public static final ResourceLocation TRINITY_WEAPON = id("trinity_weapon");
    public static final ResourceLocation SHINING_CALIBUR = id("shining_calibur");
    public static final ResourceLocation SABER_SLASH = id("saber_slash");
    public static final ResourceLocation HALBERD_SPIN = id("halberd_spin");
    public static final ResourceLocation FIRESTORM_ATTACK = id("firestorm_attack");
    public static final ResourceLocation BURNING_BOMBER = id("burning_bomber");

    public static void registerSkill(ResourceLocation id, int cooldown, ChatFormatting color) {
        String name = id.getPath();
        SkillSystem.registerSkill(
                id,
                Component.translatable("skill." + name).withStyle(color),
                cooldown
        );
    }

    public static void registerSkill(ResourceLocation id, int cooldown) {
        registerSkill(id, cooldown, ChatFormatting.WHITE);
    }
}
