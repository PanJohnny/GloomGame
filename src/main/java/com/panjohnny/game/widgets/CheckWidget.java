package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.render.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckWidget extends ClickableImageWidget{

    private boolean checked = true;
    private final BufferedImage tick;
    public CheckWidget(int x, int y) {
        super("/options/check_bg.png", x, y);

        this.tick = GloomGame.getInstance().getImageFetcher().get("/options/check_tick.png");
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
