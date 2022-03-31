package com.panjohnny.game.io;

import com.panjohnny.game.event.Event;
import com.panjohnny.game.event.EventHandler;

import java.awt.event.KeyEvent;

public class KeyboardEvent extends Event<KeyEvent> {
    public KeyboardEvent(Class<?> eventCaller, EventHandler eventHandler, KeyEvent data) {
        super(eventCaller, eventHandler, data);
    }

    public KeyboardEvent(Class<?> eventCaller, KeyEvent data) {
        super(eventCaller, data);
    }

    public boolean isKeyUp() {
        return getData().getID() == KeyEvent.KEY_RELEASED;
    }

    public boolean isKeyDown() {
        return getData().getID() == KeyEvent.KEY_PRESSED;
    }

    public int getKeyCode() {
        return getData().getKeyCode();
    }

    public char getKeyChar() {
        return getData().getKeyChar();
    }

    public boolean isShiftDown() {
        return getData().isShiftDown();
    }

    public boolean isControlDown() {
        return getData().isControlDown();
    }

    public boolean isAltDown() {
        return getData().isAltDown();
    }

    public boolean isMetaDown() {
        return getData().isMetaDown();
    }

    public boolean isAltGraphDown() {
        return getData().isAltGraphDown();
    }

}
