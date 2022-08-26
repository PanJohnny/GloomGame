package com.panjohnny.game.io;

import com.panjohnny.game.event.Event;
import com.panjohnny.game.render.Renderer;

import java.awt.event.MouseEvent;

public class MouseClickEvent extends Event<MouseEvent> {
    final int x, y;

    public MouseClickEvent(MouseEvent data) {
        super(Mouse.class, data);
        x = data.getX() / Renderer.getInstance().getScaleX();
        y = data.getY() / Renderer.getInstance().getScaleY();
    }

    public boolean isLeftClick() {
        return getData().getButton() == MouseEvent.BUTTON1;
    }

    public boolean isRightClick() {
        return getData().getButton() == MouseEvent.BUTTON3;
    }

    public boolean isMiddleClick() {
        return getData().getButton() == MouseEvent.BUTTON2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
