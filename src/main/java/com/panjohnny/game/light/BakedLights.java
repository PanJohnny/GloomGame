package com.panjohnny.game.light;

import java.awt.*;

public class BakedLights {
    public static BakedLight createRounded(int x, int y, int radius, Color color) {
        BakedLight light = new BakedLight(color, x, y) {
            @Override
            public void bake() {
                int dist = radius/2;
                createLightMap(-dist, -dist, dist, dist);
                double ad = 1.0/dist;
                for (int i= -dist; i < dist; i++) {
                    for (int j = -dist; j < dist; j++) {
                        // this should create circle with center of 0,0 when further the point is from center that alpha is closer to 0
                        double alpha = 1.0 - Math.sqrt(i*i + j*j) * ad;
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
}
