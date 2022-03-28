package com.panjohnny.game.widgets;

import com.panjohnny.game.Main;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckWidget extends ClickableImageWidget{

    private boolean checked = true;
    private final BufferedImage tick;
    public CheckWidget(int x, int y) {
        super("/options/check_bg.png", x, y);

        this.tick = Main.getInstance().getImageFetcher().get("/options/check_tick.png");
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        if(checked) {
            drawLayer(g, tick, 0, 0);
        }
    }

    @Override
    public void onInteract(Object context) {
        checked = !checked;
    }
}
