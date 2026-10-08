package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.handler;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModItems;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.MirrorAnimations;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.MirrorConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MirrorClientHandler implements IRiderClientHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    public void onDriverItemInserted(@NotNull ClientRiderContext ctx) {
        if (ctx.changedItems() == null) return;
        ItemStack stack = ctx.changedItems().entrySet().iterator().next().getValue();
        animateMirrorInsert(ctx.player(), stack);
    }

    private void animateMirrorInsert(LocalPlayer player, ItemStack stack) {
        if (stack.is(ModItems.Ryuki.RYUKI_DECK)) {
            MirrorAnimations.RYUKI_HENSHIN.play(player);
        }
    }

    @Override
    public void onDriverItemExtracted(@NotNull ClientRiderContext ctx) {
        MirrorAnimations.MIRROR_DRAW.play(ctx.player());
    }

    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();

        ResourceLocation formId = ctx.currentFormId();
        if (formId == null) return;

        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) {
            MirrorAnimations.RYUKI_READ.play(player);
        }
        // 技能动画分派
        // animateMirror(player, skillId);

        // 技能位移
        // deplaceMirror(player, skillId);
    }
}
