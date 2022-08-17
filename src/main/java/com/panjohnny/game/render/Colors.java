package com.panjohnny.game.render;

import java.awt.*;

/**
 * Color palette
 */
public class Colors {
    public static final Color DARK = new Color(0, 20, 39);
    public static final Color YELLOW = new Color(224, 203, 168);
    public static final Color RED = new Color(162, 103, 105);

    public static Color alpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
    }
}
