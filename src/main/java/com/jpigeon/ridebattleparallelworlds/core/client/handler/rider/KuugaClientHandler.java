package com.jpigeon.ridebattleparallelworlds.core.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.KuugaAnimations;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.util.SkillMovementUtils;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.DragonRodItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.item.PegasusBowgunItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public final class KuugaClientHandler implements IRiderClientHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRiderDispatcher.register(new KuugaClientHandler());
    }

    @Override
    public ResourceLocation riderId() {
        return RiderIds.KUUGA_ID;
    }

    @Override
    public void onPending(ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        if (!(ctx.driverStack().getItem() instanceof ArcleItem arcle)) return;

        boolean alreadyTransformed = RideBattleAPI.isTransformed(player);
        ResourceLocation formId = ctx.pendingFormId();

        if (player.isCrouching()) {
            setDriverAnim(ctx.driverStack(), formId);
            return;
        }

        if (alreadyTransformed) {
            KuugaAnimations.SWITCH.play(player);
        } else {
            KuugaAnimations.HENSHIN.play(player);
            if (arcle.getCurrentAnimState().equals("inBody") || arcle.getCurrentAnimState().equals("shrink")) {
                RideBattleAPI.scheduleTicks(5, arcle::triggerAppear);
            }
        }
        setDriverAnim(ctx.driverStack(), formId);
    }

    @Override
    public void postUnhenshin(ClientRiderContext ctx) {
        if (ctx.driverStack().getItem() instanceof ArcleItem arcle) {
            arcle.shrinkInBody();
        }
    }

    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();

        // 技能动画分派
        animateKuuga(player, skillId);

        // 技能位移
        deplaceKuuga(player, skillId);
    }

    private void deplaceKuuga(LocalPlayer player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROWING_KICK)) growingKick(player);
        if (skillId.equals(RiderSkills.MIGHTY_KICK)) mightyKick(player);
        if (skillId.equals(RiderSkills.RISING_MIGHTY_KICK)) risingMightyKick(player);
        if (skillId.equals(RiderSkills.AMAZING_MIGHTY_KICK)) amazingMightyKick(player);
        if (skillId.equals(RiderSkills.ULTIMATE_KICK)) ultimateKick(player);
    }

    private void animateKuuga(LocalPlayer player, ResourceLocation skillId) {
        ItemStack mainItem = player.getMainHandItem();
        if (isKuugaKick(skillId)) {
            KuugaAnimations.MIGHTY_KICK.play(player);
            RideBattleAPI.scheduleTicks(33, () -> KuugaAnimations.RESET.play(player));
            return;
        }
        if (skillId.equals(RiderSkills.SPLASH_DRAGON) || skillId.equals(RiderSkills.RISING_SPLASH_DRAGON)) {
            boolean mainHand = (mainItem.getItem() instanceof PegasusBowgunItem);
            if (mainHand) KuugaAnimations.SPLASH_MAIN.play(player);
            else KuugaAnimations.SPLASH_OFF.play(player);
        } else if (skillId.equals(RiderSkills.BLAST_PEGASUS) || skillId.equals(RiderSkills.RISING_BLAST_PEGASUS)) {
            boolean mainHand = mainItem.getItem() instanceof DragonRodItem;
            if (mainHand) KuugaAnimations.BLAST_MAIN.play(player);
            else KuugaAnimations.BLAST_OFF.play(player);
        } else if (skillId.equals(RiderSkills.CALAMITY_TITAN) || skillId.equals(RiderSkills.RISING_CALAMITY_TITAN)) {
            KuugaAnimations.CALAMITY_TITAN.play(player);
        }
    }

    private boolean isKuugaKick(ResourceLocation skillId) {
        return skillId.getPath().contains("_kick");
    }

    // 空我
    private static void growingKick(Player player) {
        SkillMovementUtils.riderKickJump(player, 1);
        SkillMovementUtils.riderKickForward(player, 1, 10);
    }

    private static void mightyKick(Player player) {
        SkillMovementUtils.riderKickJump(player, 1.1);
        SkillMovementUtils.riderKickForward(player, 1.5, 10);
    }

    private static void risingMightyKick(Player player) {
        SkillMovementUtils.riderKickJump(player, 1.2);
        SkillMovementUtils.riderKickForward(player, 2, 10);
    }

    private static void amazingMightyKick(Player player) {
        SkillMovementUtils.riderKickJump(player, 1.4);
        SkillMovementUtils.riderKickForward(player, 2.5, 15);
    }

    private static void ultimateKick(Player player) {
        SkillMovementUtils.riderKickJump(player, 1.5);
        SkillMovementUtils.riderKickForward(player, 2.5, 15);
    }

    @Override
    public void onDriverChanged(@NotNull ClientRiderContext ctx) {
    }

    private static void setDriverAnim(ItemStack driver, ResourceLocation formId) {
        if (driver.getItem() instanceof ArcleItem arcle) {
            arcle.setStateByFormId(formId);
        }
    }
}
