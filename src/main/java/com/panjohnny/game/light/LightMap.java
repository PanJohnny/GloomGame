package com.panjohnny.game.light;

import com.google.common.cache.LoadingCache;
import com.panjohnny.game.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.*;
import java.util.function.Consumer;

@Getter
@EqualsAndHashCode
public class LightMap {
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    private final double[][] map;
    public LightMap(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        map = new double[Math.abs(minX) + Math.abs(maxX) + 1][Math.abs(minY) + Math.abs(maxY) + 1];
    }

    public void set(int x, int y, double value) {
        // check if int is in bounds else throw exception
        if(isOutOfBounds(x,y))
            throw new IndexOutOfBoundsException("x%d or y%d is out of bounds".formatted(x,y));

        int realX = Math.abs(x - minX);
        int realY = Math.abs(y - minY);

        map[realX][realY] = value;
    }

    public double get(int x, int y) {
        // check if int is in bounds else throw exception
        if(isOutOfBounds(x,y))
            throw new IndexOutOfBoundsException("x%d or y%d is out of bounds".formatted(x,y));

        int realX = Math.abs(x - minX);
        int realY = Math.abs(y - minY);

        return map[realX][realY];
    }

    public boolean isOutOfBounds(int x, int y) {
        return x>maxX || x<minX || y>maxY || y<minY;
    }

    public int getWidth() {
        return Math.abs(minX) + Math.abs(maxX);
    }

    public int getHeight() {
        return Math.abs(minY) + Math.abs(maxY);
    }

    public void forEach(Consumer<Pair<Point, Double>> consumer) {
        for(int x = minX; x<=maxX; x++) {
            for(int y = minY; y<=maxY; y++) {
                consumer.accept(new Pair<>(new Point(x,y), get(x,y)));
            }
        }
    }
}
