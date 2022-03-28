package com.panjohnny.game.io;

import com.panjohnny.game.event.Event;
import com.panjohnny.game.event.EventHandler;

import java.awt.event.MouseEvent;

public class MouseClickEvent extends Event<MouseEvent> {
    public MouseClickEvent(MouseEvent data) {
        super(Mouse.class, data);
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
        return getData().getX();
    }

    public int getY() {
        return getData().getY();
    }
}
