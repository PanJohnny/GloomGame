package com.panjohnny.game;

import com.panjohnny.game.render.Drawable;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Consumer;

@Getter
public abstract class GameObject implements Drawable {
    @Setter
    private int x, y;
    @Setter
    private int width;
    @Setter
    private int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObject() {

    }

    public Rectangle getBound() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getActualBound() {
        Dimension dimension = getActualSize();
        Point p = getActualPosition();

        return new Rectangle(p.x, p.y, dimension.width, dimension.height);
    }

    public abstract void tick();

    public Dimension getActualSize() {
        return GloomGame.getInstance().getWindow().transformSize(width, height);
    }

    public Point getActualPosition() {
        return GloomGame.getInstance().getWindow().transformPos(x, y);
    }

    protected void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void apply(Consumer<GameObject> consumer) {
        consumer.accept(this);
    }
}
