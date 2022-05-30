package com.panjohnny.game.light;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.render.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bulb extends BakedLight{
    private final BufferedImage lightBulb;
    public Bulb(int x, int y) {
        super(Colors.YELLOW, x, y);

        lightBulb = GloomGame.getInstance().getImageFetcher().get("/assets/tiles/bulb.png");

        bake();
    }

    /**
     * @apiNote You should first call {@link #createLightMap(int, int, int, int)} and on the end call {@link #cache()}
     */
    @Override
    public void bake() {
        createLightMap(-8, 0, 8, 64);

        for (int i = getLightMap().getMinX(); i < getLightMap().getMaxX(); i++) {
            for (int j = getLightMap().getMinY(); j < getLightMap().getMaxY(); j++) {
                setAlpha(i, j, 0.2f);
            }
        }

        cache();
    }

    @Override
    public void draw(Graphics g) {
        Point pos = getActualPosition();

        g.drawImage(lightBulb, pos.x, pos.y, null);
        g.drawImage(getImage(), pos.x, pos.y+lightBulb.getHeight(), null);
    }
}
