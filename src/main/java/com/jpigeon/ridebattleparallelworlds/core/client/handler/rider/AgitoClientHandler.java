package com.jpigeon.ridebattleparallelworlds.core.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.AgitoAnimations;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.util.SkillMovementUtils;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor.AgitoGroundItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class AgitoClientHandler implements IRiderClientHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    public void onDriverItemInserted(@NotNull ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        Map<ResourceLocation, ItemStack> changed = ctx.changedItems();
        if (changed == null || changed.isEmpty()) return;

        // 拿这一条 change 的 stack
        ItemStack inserted = changed.values().iterator().next();

        if (inserted.is(ModItems.Agito.BURNING_ELEMENT.get())) {
            AgitoAnimations.PREP_B.play(player);
        } else {
            AgitoAnimations.PREP.play(player);
        }
    }

    @Override
    public void onPending(ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        if (!(ctx.driverStack().getItem() instanceof AlterRingItem)) return;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().stop();

        ResourceLocation formId = ctx.pendingFormId();

        if (player.isCrouching()) {
            setDriverAnim(ctx.driverStack(), formId);
            return;
        }

        AgitoAnimations.HENSHIN.play(player);

        RideBattleAPI.scheduleTicks(10, () -> setDriverAnim(ctx.driverStack(), formId));
    }

    @Override
    public void postUnhenshin(ClientRiderContext ctx) {
        if (ctx.driverStack().getItem() instanceof AlterRingItem alterRing) {
            alterRing.shrinkInBody();
        }
    }

    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();

        // 技能动画分派
        animateAgito(player, skillId);

        // 技能位移
        deplaceAgito(player, skillId);
    }

    private void deplaceAgito(LocalPlayer player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROUND_KICK)) groundKick(player);
    }

    private static void groundKick(LocalPlayer player) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.triggerOpen();
            RideBattleAPI.scheduleTicks(70, agitoGround::setClosed);
        }

        SkillMovementUtils.riderKickJump(player, 1.3, 30);
        SkillMovementUtils.riderKickForward(player, 1.8, 45);
    }

    private static void animateAgito(LocalPlayer player, ResourceLocation skillId) {
        if (skillId.equals(RiderSkills.GROUND_KICK)) {
            AgitoAnimations.KICK_PREP.play(player);
            RideBattleAPI.scheduleTicks(35, () -> AgitoAnimations.KICK.play(player));
        }
    }

    private static void setDriverAnim(ItemStack driver, ResourceLocation formId) {
        if (driver.getItem() instanceof AlterRingItem alterRingItem) {
            alterRingItem.setStateByFormId(formId);
        }
    }
}
