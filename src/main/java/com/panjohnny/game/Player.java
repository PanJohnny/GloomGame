package com.panjohnny.game;

import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.KeyboardEvent;
import com.panjohnny.game.render.AnimatedTexture;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject implements EventListener {
    private final AnimatedTexture front;
    private final AnimatedTexture runR, runL;

    public Player(int x, int y) {
        super(x, y, 32, 64);
        front = GloomGame.getInstance().getImageFetcher().getAnimation("/player/player_idle.png", 2, 1, 1000L);
        runL = GloomGame.getInstance().getImageFetcher().getAnimation("/player/player_run.png", 2, 1, 245L);
        runR = runL.getCopyFlippedHorizontally();

        run = runL;
    }

    private Point temp_pos;
    private Dimension temp_size;
    private boolean moving = false;
    private AnimatedTexture run, jump;

    @Setter
    private boolean onGround = false;

    @Override
    public void tick() {
        temp_pos = getActualPosition();
        temp_size = getActualSize();

        if(!onGround)
            velY = 5;

        setX(getX() + velX);
        setY(getY() + velY);

        moving = velX != 0;

        if (moving) {
            if (velX > 0)
                run = runR;
            else
                run = runL;
            run.update();
        } else {
            front.update();
        }

        onGround = false;
    }


    @Override
    public void draw(Graphics g) {if (!moving) {
            g.drawImage(front.getCurrentFrame(), temp_pos.x, temp_pos.y, temp_size.width, temp_size.height, null);
        } else if (run != null)
            g.drawImage(run.getCurrentFrame(), temp_pos.x, temp_pos.y, temp_size.width, temp_size.height, null);
    }

    public Player reposition(int x, int y) {
        setX(x);
        setY(y);
        return this;
    }

    @Getter
    @Setter
    private int velX = 0, velY = 0;

    @EventTarget(target = KeyboardEvent.class)
    public void onKeyboardEvent(KeyboardEvent e) {
        if (e.isKeyDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> velY = -1;
                case KeyEvent.VK_DOWN -> velY = 1;
                case KeyEvent.VK_LEFT -> velX = -1;
                case KeyEvent.VK_RIGHT -> velX = 1;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> velY = 0;
                case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> velX = 0;
            }
        }
    }
}
