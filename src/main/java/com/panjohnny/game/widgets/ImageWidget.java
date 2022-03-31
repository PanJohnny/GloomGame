package com.panjohnny.game.widgets;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWidget extends GameObject {

    @Getter
    private final BufferedImage image;

    public ImageWidget(BufferedImage image, int width, int height, int x, int y) {
        super(x, y, width, height);

        this.image = image;
    }

    public ImageWidget(BufferedImage image, int x, int y) {
        this(image, image.getWidth(), image.getHeight(), x, y);
    }

    public ImageWidget(String path, int width, int height, int x, int y) {
        super(x, y, width, height);

        this.image = GloomGame.getInstance().getImageFetcher().get(path);
        if (width == 0 && height == 0) {
            this.setSize(image.getWidth(), image.getHeight());
        }
    }

    public ImageWidget(String path, int x, int y) {
        this(path, 0, 0, x, y);
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, getActualPosition().x, getActualPosition().y, getActualSize().width, getActualSize().height, null);
    }

    private double multiplier = 0;

    public ImageWidget multiplySize(double multiplier) {
        this.setSize((int) (getWidth() * multiplier), (int) (getHeight() * multiplier));
        this.multiplier = multiplier;
        return this;
    }

    public void drawLayer(Graphics g, BufferedImage i, int xOffset, int yOffset) {
        Dimension d = GloomGame.getInstance().getWindow().transformSize(i.getWidth(), i.getHeight());
        g.drawImage(i, getActualPosition().x + xOffset, getActualPosition().y + yOffset, (int) (d.getWidth() * multiplier), (int) (d.getHeight() * multiplier), null);
    }
}
