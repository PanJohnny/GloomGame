package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.io.Mouse;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SliderWidget extends ImageWidget {
    private final BufferedImage thumb;
    private int sliderX = 10;
    public SliderWidget(int x, int y) {
        super("/options/slider.png", x, y);

        this.thumb = GloomGame.getInstance().getImageFetcher().get("/options/slider_front.png");
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        drawLayer(g, thumb, sliderX, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (Mouse.getInstance().isButtonLeftDown() && getActualBound().contains(Mouse.getInstance().getX(), Mouse.getInstance().getY())) {
            sliderX = Mouse.getInstance().getX() - thumb.getWidth() * 2;
        }
    }
}
