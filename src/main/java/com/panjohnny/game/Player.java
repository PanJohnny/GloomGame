package com.panjohnny.game;

import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.KeyboardEvent;
import com.panjohnny.game.render.AnimatedTexture;
import com.panjohnny.game.util.Timer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import static com.panjohnny.game.io.KeyManager.*;

public class Player extends GameObject implements EventListener {
    private final AnimatedTexture front;
    private final AnimatedTexture runR, runL;

    private final Timer<Player> jumpTimer;

    public Player(int x, int y) {
        super(x, y, 32, 64);
        front = GloomGame.getInstance().getImageFetcher().getAnimation("/assets/player/player_idle.png", 2, 1, 1000L);
        runL = GloomGame.getInstance().getImageFetcher().getAnimation("/assets/player/player_run.png", 2, 1, 245L);
        runR = runL.getCopyFlippedHorizontally();
        temp_pos = new Point(0, 0);
        run = runL;
        jumpTimer = Timer.createDummyTimer((a) -> a.setJumping(false), 300L);
    }

    private Point temp_pos;
    private Dimension temp_size;
    private boolean moving = false;
    private AnimatedTexture run;

    @Setter
    private boolean onGround = false;

    @Getter
    @Setter
    private boolean jumping = false;

    @Override
    public void tick() {
        temp_pos = getActualPosition();
        temp_size = getActualSize();

        jumpTimer.tick(this);

        if (jumping)
            velY = -2;

        if (!onGround && !jumping) {
            velY = 5;
        }

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
    public void draw(Graphics g) {
        if (!moving) {
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

    @EventTarget(KeyboardEvent.class)
    public void onKeyboardEvent(KeyboardEvent e) {
        if (e.isKeyDown()) {
            if (isOf(JUMP_KEYS, e.getKeyCode())) {
                if (onGround) {
                    jumping = true;
                    jumpTimer.reset();
                }
            }
            if (isOf(MOVE_LEFT_KEYS, e.getKeyCode())) {
                velX = -1;
            } else if (isOf(MOVE_RIGHT_KEYS, e.getKeyCode())) {
                velX = 1;
            }
        } else {
            if (isOf(MOVE_LEFT_KEYS, e.getKeyCode()) || isOf(MOVE_RIGHT_KEYS, e.getKeyCode())) {
                velX = 0;
            }

            if (isOf(JUMP_KEYS, e.getKeyCode())) {
                jumping = false;
                jumpTimer.stop();
            }
        }
    }
}
