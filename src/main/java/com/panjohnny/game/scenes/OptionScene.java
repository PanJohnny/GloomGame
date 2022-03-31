package com.panjohnny.game.scenes;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
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
        add(new SliderWidget(10, 100).multiplySize(2));
        add(new CheckWidget(10, 200));
        add(new TextWidget(400, 100, "wavta", Colors.YELLOW, 40));
        return this;
    }

    @Override
    public void onKeyPress(int key) {

    }

    @Override
    public void onKeyRelease(int key) {

    }

    @Override
    public void update() {
        super.update();
    }

    @EventTarget(target = MouseClickEvent.class)
    public void click(MouseClickEvent event) {
        if(event.isLeftClick()) {
            for (Drawable d : getOfType(CheckWidget.class)) {
                if(WidgetUtil.isMouseOver((GameObject) d)){
                    ClickableImageWidget ciw = (ClickableImageWidget) d;
                    ciw.onInteract(ciw);
                }
            }
        }
    }
}
