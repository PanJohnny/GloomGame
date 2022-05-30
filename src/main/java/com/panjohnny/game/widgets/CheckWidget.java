package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckWidget extends ClickableImageWidget{

    private boolean checked = true;
    private final BufferedImage tick;
    public CheckWidget(int x, int y) {
        super("/assets/menu/widgets/check/check_bg.png", x, y);

        this.tick = GloomGame.getInstance().getImageFetcher().get("/assets/menu/widgets/check/check_tick.png");
        setOnClick(this::tick);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        if(checked) {
            drawLayer(g, tick, 0, 0);
        }
    }

    public void tick(Object o) {
        checked = !checked;
    }
}
