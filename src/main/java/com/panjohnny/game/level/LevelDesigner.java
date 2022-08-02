package com.panjohnny.game.level;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.widgets.ButtonWidget;
import com.panjohnny.game.widgets.ClickableImageWidget;
import com.panjohnny.game.widgets.Widget;
import com.panjohnny.game.widgets.WidgetUtil;

public class LevelDesigner extends Scene implements EventListener {
    LevelManager manager;

    @Override
    public Scene init() {
        // TODO Fix level loading! And also look at graphics you can scale natively!
        GloomGame.registerEventListener(this);
        manager = new LevelManager(LevelParser.parseInternal("test.plf"));
        ButtonWidget test = new ButtonWidget(10, 10, (b) -> manager.load(0), buttonWidgetGraphicsPair -> ButtonWidget.overlay(buttonWidgetGraphicsPair, Colors.RED), "Test").multiplySize(2);
        add(test);
        return this;
    }

    @EventTarget(MouseClickEvent.class)
    public void click(MouseClickEvent event) {
        if (event.isLeftClick()) {
            for (Drawable d : getOfType(ClickableImageWidget.class)) {
                if (WidgetUtil.isMouseOver((Widget) d)) {
                    ClickableImageWidget ciw = (ClickableImageWidget) d;
                    ciw.onInteract(ciw);
                }
            }
        }
    }
}
