package com.panjohnny.game.widgets;

import com.panjohnny.game.io.Mouse;

public class WidgetUtil {
    public static boolean isMouseOver(Widget widget) {
        return widget.getBound().contains(Mouse.getInstance().getFixedX(), Mouse.getInstance().getFixedY());
    }
}
