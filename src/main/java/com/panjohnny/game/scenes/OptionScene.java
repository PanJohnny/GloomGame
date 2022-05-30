package com.panjohnny.game.scenes;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.widgets.*;

public class OptionScene extends Scene implements EventListener {
    @Override
    public Scene init() {
        GloomGame.registerEventListener(this);
        add(TextWidget.translated(10, 70, "options.volume", Colors.YELLOW, 50));
        add(new SliderWidget(10, 100, (a) -> System.out.printf("Volume: %s%n",a.toFloat())).multiplySize(2));
        add(new CheckWidget(10, 200));
        add(TextWidget.translated(400, 50, "menu.options", Colors.YELLOW, 100));
        ButtonWidget back = new ButtonWidget(370, 400, (b) -> GloomGame.getInstance().setScene(0), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.back")).multiplySize(2);
        add(back);

        return this;
    }

    @Override
    public void update() {
        super.update();
    }

    @EventTarget(MouseClickEvent.class)
    public void click(MouseClickEvent event) {
        if(event.isLeftClick()) {
            for (Drawable d : getOfType(ClickableImageWidget.class)) {
                if(WidgetUtil.isMouseOver((Widget) d)){
                    ClickableImageWidget ciw = (ClickableImageWidget) d;
                    ciw.onInteract(ciw);
                }
            }
        }
    }
}
