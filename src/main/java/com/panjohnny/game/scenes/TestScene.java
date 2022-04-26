package com.panjohnny.game.scenes;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.tile.Tile;
import com.panjohnny.game.widgets.TextWidget;

import java.awt.*;

public class TestScene extends Scene {
    @Override
    public Scene init() {
        GloomGame.getInstance().getRenderer().setBackground(Color.BLACK);
        add(new TextWidget(100, 100, "This is just test scene", Colors.YELLOW, 60));

        add(GloomGame.getInstance().getPlayer());
        add(new Tile(0, 200, 640, 640));

        return this;
    }
}
