package com.panjohnny.game.widgets;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ClickableImageWidget extends ImageWidget implements InteractionWidget<Object> {
    private Consumer<ClickableImageWidget> onClick;

    public ClickableImageWidget(BufferedImage image, int width, int height, int x, int y) {
        super(image, width, height, x, y);
    }

    public ClickableImageWidget(BufferedImage image, int x, int y) {
        super(image, x, y);
    }

    public ClickableImageWidget(String path, int width, int height, int x, int y) {
        super(path, width, height, x, y);
    }

    public ClickableImageWidget(String path, int x, int y) {
        super(path, x, y);
    }

    @Override
    public void onInteract(Object context) {
        onClick.accept(this);
    }

    public ClickableImageWidget setOnClick(Consumer<ClickableImageWidget> onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public ClickableImageWidget multiplySize(double scale) {
        super.multiplySize(scale);
        return this;
    }
}
