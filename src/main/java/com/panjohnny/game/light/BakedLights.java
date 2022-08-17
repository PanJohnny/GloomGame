package com.panjohnny.game.light;

import java.awt.*;

/**
 * I am not sure if these are baked lights but, they are pretty cool...
 */
public class BakedLights {
    // TODO fix this and make it more efficient
    public static BakedLight createRounded(int x, int y, int radius, Color color) {
        BakedLight light = new BakedLight(color, x, y) {
            @Override
            public void bake() {
                int dist = radius / 2;
                createLightMap(-dist, -dist, dist, dist);
                double ad = 1.0 / dist;
                for (int i = -dist; i < dist; i++) {
                    for (int j = -dist; j < dist; j++) {
                        // this should create circle with center of 0,0 when further the point is from center that alpha is closer to 0
                        double alpha = 1.0 - Math.sqrt(i * i + j * j) * ad;
                        if (alpha > 1.0) alpha = 1.0;
                        else if (alpha < 0.0) alpha = 0.0;

                        setAlpha(i, j, alpha);
                    }
                }

                cache();
            }
        };

        light.bake();
        return light;
    }

    public static BakedLight createRectangle(int x, int y, int width, int height, Color color) {
        BakedLight light = new BakedLight(color, x, y) {
            @Override
            public void bake() {
                createLightMap(-width / 2, 0, width / 2, height);

                for (int i = getLightMap().getMinX(); i < getLightMap().getMaxX(); i++) {
                    for (int j = getLightMap().getMinY(); j < getLightMap().getMaxY(); j++) {
                        int fromTop = getLightMap().getMinY() + j;

                        double alpha = 1.0 - (fromTop - getLightMap().getMinY()) / (double) (getLightMap().getMaxY() - getLightMap().getMinY());
                        if (alpha > 1.0) alpha = 1.0;
                        else if (alpha < 0.0) alpha = 0.0;

                        setAlpha(i, j, alpha);
                    }
                }

                cache();
            }
        };

        light.bake();
        return light;
    }

    public static BakedLight createTriangular(int x, int y, int width, int height, Color color) {
        BakedLight light = new BakedLight(color, x, y) {
            @Override
            public void bake() {
                createLightMap(-width / 2, 0, width / 2, height);

                for (int i = getLightMap().getMinX(); i < getLightMap().getMaxX(); i++) {
                    for (int j = getLightMap().getMinY(); j < getLightMap().getMaxY(); j++) {
                        int fromTop = getLightMap().getMinY() + j;
                        int fromLeft = -(fromTop / (height / width));
                        int fromRight = -fromLeft;

                        double alpha = 1.0 - (fromTop - getLightMap().getMinY()) / (double) (getLightMap().getMaxY() - getLightMap().getMinY());
                        if (alpha > 1.0) alpha = 1.0;
                        else if (alpha < 0.0) alpha = 0.0;

                        if (i < fromLeft || i > fromRight) {
                            alpha = 0.0;
                        }

                        setAlpha(i, j, alpha);
                    }
                }

                cache();
            }
        };

        light.bake();
        return light;
    }
}
