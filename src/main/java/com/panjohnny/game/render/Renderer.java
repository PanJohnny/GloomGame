package com.panjohnny.game.render;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class Renderer extends Canvas {
    private final static Renderer INSTANCE = new Renderer();

    public static Renderer getInstance() {
        return INSTANCE;
    }

    public void render(LinkedList<Drawable> drawables) {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Colors.DARK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Drawable drawable : drawables) {
            drawable.draw(g);
        }

        g.dispose();
        bs.show();
    }
}
