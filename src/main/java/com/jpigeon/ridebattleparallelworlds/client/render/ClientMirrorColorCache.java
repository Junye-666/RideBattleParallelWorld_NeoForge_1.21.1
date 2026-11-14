package com.jpigeon.ridebattleparallelworlds.client.render;

public final class ClientMirrorColorCache {
    private ClientMirrorColorCache() {
    }

    private static volatile int grassColor = -1;

    public static void update(int color) {
        grassColor = color;
    }

    public static int getGrassColor() {
        return grassColor;
    }

    public static void clear() {
        grassColor = -1;
    }
}