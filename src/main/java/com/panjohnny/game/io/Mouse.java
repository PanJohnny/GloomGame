package com.panjohnny.game.io;

import com.panjohnny.game.GloomGame;
import lombok.Getter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    private static Mouse instance;

    public static void init(GloomGame gloomGame) {
        instance = new Mouse(gloomGame);
    }

    public static Mouse getInstance() {
        return instance;
    }

    @Getter
    private int x, y;

    @Getter
    private boolean shiftDown, ctrlDown, altDown;

    @Getter
    private boolean buttonLeftDown, buttonRightDown, buttonMiddleDown;

    public Mouse(GloomGame gloomGame) {
        gloomGame.getRenderer().addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        syncPos(e);

        GloomGame.fireEvent(new MouseClickEvent(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        syncPos(e);

        buttonDown(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        syncPos(e);

        buttonUp(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        syncPos(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        syncPos(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        syncPos(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        syncPos(e);
    }

    private void syncPos(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        // sync shift, ctrl, alt
        shiftDown = e.isShiftDown();
        ctrlDown = e.isControlDown();
        altDown = e.isAltDown();
    }

    private void buttonDown(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> buttonLeftDown = true;
            case MouseEvent.BUTTON2 -> buttonMiddleDown = true;
            case MouseEvent.BUTTON3 -> buttonRightDown = true;
        }
    }

    private void buttonUp(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> buttonLeftDown = false;
            case MouseEvent.BUTTON2 -> buttonMiddleDown = false;
            case MouseEvent.BUTTON3 -> buttonRightDown = false;
        }
    }
}
