package com.panjohnny.game.widgets;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.io.Mouse;

public class WidgetUtil {
    public static boolean isMouseOver(GameObject gameObject) {
        return gameObject.getActualBound().contains(Mouse.getInstance().getX(), Mouse.getInstance().getY());
    };
}
