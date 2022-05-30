package com.panjohnny.game.scenes;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.widgets.*;

public class MainMenu extends Scene implements EventListener {

    @Override
    public MainMenu init() {
        reset();
        GloomGame.registerEventListener(this);
        ImageWidget logo = new ImageWidget("/assets/menu/logo.png", 240, 10).multiplySize(2);

        ButtonWidget quit = new ButtonWidget(370, 325, (b) -> System.exit(0), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.quit")).multiplySize(2);

        ButtonWidget options = new ButtonWidget(370, 250, (b) -> GloomGame.getInstance().setScene(1), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.options")).multiplySize(2);

        ButtonWidget play = new ButtonWidget(370, 175, (b) -> GloomGame.getInstance().setScene(2), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.play")).multiplySize(2);

        add(TextWidget.translated(10, 425, "dev.info", Colors.YELLOW, 40));
        add(logo);
        add(quit);
        add(options);
        add(play);
        return this;
    }

    @Override
    public void update() {
        super.update();
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
