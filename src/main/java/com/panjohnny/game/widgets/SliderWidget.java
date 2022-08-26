package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.util.MathUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class SliderWidget extends ImageWidget {
    private final BufferedImage thumb;
    private final Consumer<SliderWidget> onUpdate;
    private int sliderX = 0;

    public SliderWidget(int x, int y, Consumer<SliderWidget> onUpdate) {
        super("/assets/menu/widgets/slider/slider.png", x, y);

        this.onUpdate = onUpdate;

        this.thumb = GloomGame.getInstance().getImageFetcher().get("/assets/menu/widgets/slider/slider_front.png");
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        drawLayer(g, thumb, sliderX, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (Mouse.getInstance().isButtonLeftDown() && getBound().contains(Mouse.getInstance().getFixedX(), Mouse.getInstance().getFixedY())) {
            sliderX = Mouse.getInstance().getFixedX() - getX();
            if (onUpdate != null)
                onUpdate.accept(this);
        }

        sliderX = MathUtils.clamp(sliderX, 0, getWidth() - thumb.getWidth());
    }

    public int getValue() {
        // return value 0 - 100
        return sliderX / (getWidth() - thumb.getWidth()) * 100;
    }

    public float toFloat() {
        return Math.min((float) getValue() / 100f, 1.0f);
    }
}
