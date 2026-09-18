package com.jpigeon.ridebattleparallelworlds.core.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.ClientRiderDispatcher;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.AgitoAnimations;
import com.jpigeon.ridebattleparallelworlds.core.client.handler.util.SkillMovementUtils;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor.AgitoGroundItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public final class AgitoClientHandler implements IRiderClientHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRiderDispatcher.register(new AgitoClientHandler());
    }

    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    public void onPending(ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        if (!(ctx.driverStack().getItem() instanceof AlterRingItem)) return;

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

    @Override
    public void onDriverItemInserted(@NotNull ClientRiderContext ctx) {
        ItemStack stack = ctx.driverStack();
        LocalPlayer player = ctx.player();
        if (stack.is(ModItems.BURNING_ELEMENT.get())) AgitoAnimations.PREP_B.play(player);
        else AgitoAnimations.PREP.play(player);
    }

    private static void setDriverAnim(ItemStack driver, ResourceLocation formId) {
        if (driver.getItem() instanceof AlterRingItem alterRingItem) {
            alterRingItem.setStateByFormId(formId);
        }
    }
}
