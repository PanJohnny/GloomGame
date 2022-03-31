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
    private final AnimatedTexture jumpR, jumpL;

    public Player(int x, int y) {
        super(x, y, 32, 64);
        front = GloomGame.getInstance().getImageFetcher().getAnimation("/player/player_idle.png", 2, 1, 1000L);
        runL = GloomGame.getInstance().getImageFetcher().getAnimation("/player/player_run.png", 2, 1, 245L);
        jumpR = GloomGame.getInstance().getImageFetcher().getAnimation("/player/player_jump.png", 6, 1, 100L);
        jumpL = jumpR.getCopyFlippedHorizontally();
        runR = runL.getCopyFlippedHorizontally();

        jump = jumpL;
        run = runL;
    }

    private Point temp_pos;
    private Dimension temp_size;
    private boolean moving = false, jumping = false;
    private AnimatedTexture run, jump;
    private static final long AIR_TIME_MAX = 1000L * 1000000L;
    private long airTimeStart = 0L;

    @Setter
    private boolean onGround = false;

    @Override
    public void tick() {
        temp_pos = getActualPosition();
        temp_size = getActualSize();

        // gravity
        if(!jumping) {
            velY = 4;
        } else {
            if(airTimeStart == 0L && onGround) {
                airTimeStart = System.nanoTime();
                onGround = false;
            }

            if(!onGround) {
                velY = -2;
            }
        }

        if(airTimeStart!=0 && airTimeStart + AIR_TIME_MAX < System.nanoTime()) {
            jumping = false;
            airTimeStart = 0L;
        }

        setX(getX() + velX);
        setY(getY() + velY);

        moving = velX != 0;

        if (moving) {
            if(jumping) {
                if (velX > 0)
                    jump = jumpR;
                else
                    jump = jumpL;
            }
            if (velX > 0)
                run = runR;
            else
                run = runL;
            run.update();
        } else {
            front.update();
        }

        if(jumping)
            jump.update();
    }


    @Override
    public void draw(Graphics g) {
        if(jumping) {
            g.drawImage(jump.getCurrentFrame(), temp_pos.x, temp_pos.y, temp_size.width, temp_size.height, null);
        } else if (!moving) {
            g.drawImage(front.getCurrentFrame(), temp_pos.x, temp_pos.y, temp_size.width, temp_size.height, null);
        } else if(run != null)
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
                case KeyEvent.VK_SPACE -> jumping = true;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> velY = 0;
                case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> velX = 0;
                case KeyEvent.VK_SPACE -> jumping = false;
            }
        }
    }
}
