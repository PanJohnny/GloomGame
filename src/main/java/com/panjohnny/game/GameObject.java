package com.panjohnny.game;

import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.render.Renderer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Consumer;

@Data
@Getter
public abstract class GameObject implements Drawable, Cloneable {

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

    /**
     * @return the scaled bound of the object
     */
    public Rectangle getBound() {
        return new Rectangle(getScaledX(), getScaledY(), getScaledWidth(), getScaledHeight());
    }

    public abstract void tick();

    protected void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public GameObject apply(Consumer<GameObject> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public GameObject clone() {
        try {
            GameObject clone = (GameObject) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.setX(getX());
            clone.setY(getY());
            clone.setWidth(getWidth());
            clone.setHeight(getHeight());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public int getScaledX() {
        return x * Renderer.getInstance().getScaleX();
    }

    public int getScaledY() {
        return y * Renderer.getInstance().getScaleY();
    }

    public int getScaledWidth() {
        return width * Renderer.getInstance().getScaleX();
    }

    public int getScaledHeight() {
        return height * Renderer.getInstance().getScaleY();
    }
}
