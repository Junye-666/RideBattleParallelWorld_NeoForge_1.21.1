package com.jpigeon.ridebattleparallelworlds.client.handler.rider;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.client.anim.rider.DecadeAnimations;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecaDriverItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class DecadeClientHandler implements IRiderClientHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    public void onPending(@NotNull ClientRiderContext ctx) {
        LocalPlayer player = ctx.player();
        DecadeAnimations.INSERT_CARD.play(player);
    }

    @Override
    public void onDriverItemInserted(@NotNull ClientRiderContext ctx) {
        ItemStack driver = ctx.driverStack();
        if (driver.getItem() instanceof DecaDriverItem decaDriver) {
            decaDriver.triggerOpen();
        }
    }

    @Override
    public void onDriverItemExtracted(@NotNull ClientRiderContext ctx) {
        ItemStack driver = ctx.driverStack();
        if (driver.getItem() instanceof DecaDriverItem decaDriver) {
            decaDriver.triggerClose();
        }
    }

    @Override
    public void onSkill(ClientRiderContext ctx) {
        ResourceLocation skillId = ctx.skillId();
        if (skillId == null) return;
        LocalPlayer player = ctx.player();
    }
}
