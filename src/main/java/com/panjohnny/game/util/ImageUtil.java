package com.panjohnny.game.util;

import com.google.common.collect.ImmutableList;
import com.panjohnny.game.GloomGame;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public final class ImageUtil {
    public static BufferedImage combine(BufferedImage... images) {
        int width = 0;
        int height = 0;

        for (BufferedImage image : images) {
            if(image.getWidth()>width) {
                width=image.getWidth();
            }

            if(image.getHeight()>height) {
                height=image.getHeight();
            }
        }

        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (BufferedImage image : images) {
            combined.getGraphics().drawImage(image, 0, 0, null);
        }

        return combined;
    }

    public static BufferedImage combine(String... paths) {
        List<BufferedImage> images = new LinkedList<>();
        for (String path : paths) {
            images.add(GloomGame.getInstance().getImageFetcher().get(path));
        }
        return combine(images.toArray(new BufferedImage[0]));
    }
}
