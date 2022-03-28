package com.panjohnny.game.scenes;

import com.panjohnny.game.Main;
import com.panjohnny.game.light.BakedLights;
import com.panjohnny.game.render.Colors;

import java.awt.*;

public class TestScene extends Scene{
    @Override
    public Scene init() {
        Main.getInstance().getRenderer().setBackground(Color.BLACK);
        addDrawable(BakedLights.createRounded(400, 400, 10, Colors.YELLOW));
        return this;
    }

    @Override
    public void onKeyPress(int key) {

    }

    @Override
    public void onKeyRelease(int key) {

    }
}
