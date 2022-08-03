package com.panjohnny.game.light;

import com.panjohnny.game.GameObject;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BakedLight extends GameObject {
    @Getter
    private final Color color;
    @Getter
    private final int x, y;

    @Getter
    private LightMap lightMap;

    @Getter
    private BufferedImage image;

    public BakedLight(Color color, int x, int y) {
        super(x, y, 0, 0);
        this.color = color;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    @Override
    public void tick() {

    }

    /**
     * @apiNote You should first call {@link #createLightMap(int, int, int, int)} and on the end call {@link #cache()}
     */
    public abstract void bake();

    public void cache() {
        image = new BufferedImage(lightMap.getWidth(), lightMap.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        lightMap.forEach((p -> {
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (p.second() * 255)));
            g.fillRect(p.first().x, p.first().y, 1, 1);
        }));
        g.dispose();

        setWidth(lightMap.getWidth());
        setHeight(lightMap.getHeight());
    }

    public void create() {

    }

    public void createLightMap(int minX, int minY, int maxX, int maxY) {
        lightMap = new LightMap(minX, minY, maxX, maxY);
    }

    protected void setAlpha(int x, int y, double alpha) {
        lightMap.set(x, y, alpha);
    }
}
