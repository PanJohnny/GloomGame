package com.panjohnny.game.util;

public final class MathUtils {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
