package com.jpigeon.ridebattleparallelworlds.core.client.anim;

import net.minecraft.client.player.LocalPlayer;

public class AnimId {
    private final String path;
    private final int defaultFadeTicks;

    private AnimId(String path, int defaultFadeTime) {
        this.path = path;
        this.defaultFadeTicks = defaultFadeTime;
    }


    public static AnimId of(String path, int defaultFadeTime) {
        return new AnimId(path, defaultFadeTime);
    }

    public void play(LocalPlayer player) {
        RiderAnimations.play(player, this);
    }

    public String path() {
        return path;
    }

    public int defaultFadeTicks() {
        return defaultFadeTicks;
    }
}
