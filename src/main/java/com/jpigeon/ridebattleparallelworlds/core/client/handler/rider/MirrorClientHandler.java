package com.jpigeon.ridebattleparallelworlds.core.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.MirrorAnimations;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public final class MirrorClientHandler implements IRiderClientHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    public void onPending(ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        if (!(ctx.driverStack().getItem() instanceof AlterRingItem)) return;

        ResourceLocation formId = ctx.pendingFormId();

        if (formId != null) {
            animateMirrorHenshin(player, formId);
        }
    }

    private void animateMirrorHenshin(LocalPlayer player, ResourceLocation formId) {
        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) MirrorAnimations.RYUKI_HENSHIN.play(player);
    }


    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();

        // 技能动画分派
        // animateMirror(player, skillId);

        // 技能位移
        // deplaceMirror(player, skillId);
    }

}
