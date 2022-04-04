package com.panjohnny.game.util;

public class MathUtils {
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double gravitationalForce(double mass1, double mass2, double distance) {
        return (mass1 * mass2) / (distance * distance);
    }

    public static double gravitationalVelocity(double mass1, double mass2, double distance) {
        return Math.sqrt((2 * mass1 * mass2) / distance);
    }

    public static double getAngle(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1);
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
