package com.panjohnny.game.level;

import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.widgets.ClickableImageWidget;
import com.panjohnny.game.widgets.Widget;
import com.panjohnny.game.widgets.WidgetUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

@Builder(builderClassName = "LevelBuilder")
public class Level extends Scene implements EventListener {
    private final Consumer<Level> init;
    @Getter
    private final String name, author;

    public Level(Consumer<Level> init, String name, String author) {
        this.init = init;
        this.name = name;
        this.author = author;
    }

    @Override
    public Scene init() {
        reset();
        init.accept(this);
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
