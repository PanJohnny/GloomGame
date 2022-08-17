package com.panjohnny.game.level;

import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.io.SoundPlayer;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.widgets.ClickableImageWidget;
import com.panjohnny.game.widgets.Widget;
import com.panjohnny.game.widgets.WidgetUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class Level extends Scene implements EventListener {
    // TODO resolve level issues...

    private final Consumer<Level> init;
    @Getter
    private final String name, author;

    @Getter
    @Setter
    private String song;

    @Builder(builderClassName = "LevelBuilder")
    public Level(Consumer<Level> init, String name, String author) {
        this.init = init;
        this.name = name;
        this.author = author;
    }

    @Override
    public Scene init() {
        reset();
        init.accept(this);
        // if song is present, play it
        if (song != null) {
            SoundPlayer.playSound("/assets/music/" + song + ".wav");
        } else {
            SoundPlayer.playSound("/assets/music/theme_song.wav");
        }
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
