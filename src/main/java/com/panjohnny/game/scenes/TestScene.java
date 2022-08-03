package com.panjohnny.game.scenes;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.light.BakedLights;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.tile.Tile;
import com.panjohnny.game.widgets.*;

public class TestScene extends Scene implements EventListener {
    @Override
    public Scene init() {
        add(new TextWidget(100, 100, "This is just test scene", Colors.YELLOW, 60));
        add(new Tile(0, 200, 640, 640));

        // addp
        add(GloomGame.getInstance().getPlayer());
        GloomGame.getInstance().getPlayer().setX(10);
        GloomGame.getInstance().getPlayer().setY(10);

        // bl-round
        add(BakedLights.createRounded(100, 100, 100, Colors.RED));

        // overlay
        add(new ButtonWidget(600, 20, (b) -> GloomGame.getInstance().setScene(0), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.back")).multiplySize(2));

        // events
        GloomGame.registerEventListener(this);
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
