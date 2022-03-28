package com.panjohnny.game.light;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.panjohnny.game.render.Drawable;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public abstract class BakedLight implements Drawable {
    @Getter
    private final Color color;
    @Getter
    private final int x, y;

    @Getter
    private LightMap lightMap;

    private final LoadingCache<Point, Color> cache;

    public BakedLight(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;

        CacheLoader<Point, Color> loader = new CacheLoader<>() {
            @Override
            @NonNull
            public Color load(@NonNull Point point) throws Exception {
                // if point is out of bounds return 0
                if (lightMap.isOutOfBounds(point.x, point.y)) {
                    // return color with 0 alpha
                    return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
                }
                // return color with alpha based on lightMap
                return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (lightMap.get(point.x, point.y) * 255));
            }
        };

        cache = CacheBuilder.newBuilder().build(loader);
    }

    @Override
    public void draw(Graphics g) {
        for (int x = lightMap.getMinX(); x < lightMap.getMaxX(); x++) {
            for (int y = lightMap.getMinY(); y < lightMap.getMaxY(); y++) {
                try {
                    g.setColor(cache.get(new Point(x, y)));
                    g.fillRect(x + this.x, y + this.y, 1, 1);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @apiNote You should first call {@link #createLightMap(int, int, int, int)} and on the end call {@link #cache()}
     */
    public abstract void bake();

    public void cache() {
        // loop through lightMap and cache all values
        lightMap.cache(cache);
    }

    public void createLightMap(int minX, int minY, int maxX, int maxY) {
        lightMap = new LightMap(minX, minY, maxX, maxY);
    }

    protected void setAlpha(int x, int y, double alpha) {
        lightMap.set(x, y, alpha);
    }
}
