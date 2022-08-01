package com.panjohnny.game.scenes.designer;

import com.panjohnny.game.data.Translator;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.widgets.ButtonWidget;

public class LevelDesigner extends Scene {
    @Override
    public Scene init() {
        ButtonWidget quit = new ButtonWidget(370, 325, (b) -> System.exit(0), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 1f)), Translator.translate("btn.quit")).multiplySize(2);

        add(quit);
        return this;
    }
}
