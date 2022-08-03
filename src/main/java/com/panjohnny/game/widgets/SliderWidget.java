package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.util.MathUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class SliderWidget extends ImageWidget {
    private final BufferedImage thumb;
    private int sliderX = 0;
    private final Consumer<SliderWidget> onUpdate;

    public SliderWidget(int x, int y, Consumer<SliderWidget> onUpdate) {
        super("/assets/menu/widgets/slider/slider.png", x, y);

        this.onUpdate = onUpdate;

        this.thumb = GloomGame.getInstance().getImageFetcher().get("/assets/menu/widgets/slider/slider_front.png");
    }

    public SliderWidget(int x, int y) {
        this(x, y, null);
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
            sliderX = Mouse.getInstance().getX() - getActualPosition().x;
            if (onUpdate != null)
                onUpdate.accept(this);
        }

        sliderX = MathUtils.clamp(sliderX, 0, (int) (getActualSize().width - GloomGame.getInstance().getWindow().multiply(thumb.getWidth())));
    }

    public int getValue() {
        // return value 0 - 100
        return (int) ((sliderX / (getActualSize().width - GloomGame.getInstance().getWindow().multiply(thumb.getWidth()))) * 100);
    }

    public float toFloat() {
        return Math.min((float) getValue() / 100f, 1.0f);
    }
}
