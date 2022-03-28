package com.panjohnny.game;

import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.render.Window;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
public abstract class GameObject implements Drawable {
    @Setter
    private int x,y;
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

    public Rectangle getBound() {
        return new Rectangle(x,y,width,height);
    }

    public Rectangle getActualBound() {
        Dimension dimension = getActualSize();
        Point p = getActualPosition();

        return new Rectangle(p.x, p.y, dimension.width, dimension.height);
    }

    public abstract void tick();

    public Dimension getActualSize() {
        return Main.getInstance().getWindow().transformSize(width, height);
    }

    public Point getActualPosition() {
        return Main.getInstance().getWindow().transformPos(x, y);
    }

    protected void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }
}
