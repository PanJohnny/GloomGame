package com.panjohnny.game.level;

import com.panjohnny.game.render.Colors;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.widgets.TextWidget;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class LoadingScene extends Scene {
    private Level nextLevel;
    private LevelManager manager;
    @Override
    public Scene init() {
        add(TextWidget.translated(200, 100, "menu.loading", new Random().nextBoolean()? Colors.RED:Colors.YELLOW, 60));
        new Thread(() -> {
            nextLevel.init();
            manager.change(nextLevel);
        }, "LVL-LOAD");
        return this;
    }
}
