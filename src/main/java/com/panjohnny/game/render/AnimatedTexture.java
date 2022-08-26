package com.panjohnny.game.render;

import lombok.Getter;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to make more images from one image.
 *
 * @implNote Nice
 */
@Getter
public class AnimatedTexture {
    private final BufferedImage image;
    private final int columns;
    private final int rows;
    private final List<BufferedImage> frames;
    /**
     * In millis
     */
    private final long timeBetweenFrames;
    private int currentFrame;
    private long lastTime;

    public AnimatedTexture(BufferedImage image, int columns, int rows, long timeBetweenFrames) {
        this.image = image;
        this.columns = columns;
        this.rows = rows;
        int width = image.getWidth() / columns;
        int height = image.getHeight() / rows;

        frames = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames.add(image.getSubimage(j * width, i * height, width, height));
            }
        }

        this.timeBetweenFrames = timeBetweenFrames;
        lastTime = System.nanoTime();
    }

    public void next() {
        currentFrame++;
        if (currentFrame >= frames.size()) {
            currentFrame = 0;
        }
    }

    public void update() {
        if (System.nanoTime() - lastTime > timeBetweenFrames * 1000000) {
            next();
            lastTime = System.nanoTime();
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames.get(currentFrame);
    }

    public AnimatedTexture getCopyFlippedHorizontally() {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        op.filter(image, flipped);
        return new AnimatedTexture(flipped, columns, rows, timeBetweenFrames);
    }
}
