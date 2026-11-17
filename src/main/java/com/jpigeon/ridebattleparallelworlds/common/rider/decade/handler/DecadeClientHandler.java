package com.jpigeon.ridebattleparallelworlds.common.rider.decade.handler;

import com.jpigeon.ridebattlelib.common.api.client.ClientRiderContext;
import com.jpigeon.ridebattlelib.common.api.client.IRiderClientHandler;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecadeAnimations;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
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
}
