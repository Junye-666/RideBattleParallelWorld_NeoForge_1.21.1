package com.jpigeon.ridebattleparallelworlds.client.anim;

import com.jpigeon.ridebattleparallelworlds.client.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.player.LocalPlayer;

public final class RiderAnimations {
    private RiderAnimations() {}

    public static void play(LocalPlayer player, AnimId id) {
        PlayerAnimationTrigger.playAnimation(
                player, id.path(), id.defaultFadeTicks());
    }

    public static void play(LocalPlayer player, AnimId id, int fade) {
        PlayerAnimationTrigger.playAnimation(
                player, id.path(), fade);
    }
}
