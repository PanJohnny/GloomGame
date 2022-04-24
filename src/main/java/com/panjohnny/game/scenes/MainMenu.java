package com.panjohnny.game.scenes;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.widgets.ButtonWidget;
import com.panjohnny.game.widgets.ClickableImageWidget;
import com.panjohnny.game.widgets.ImageWidget;
import com.panjohnny.game.widgets.WidgetUtil;

import java.awt.*;

public class MainMenu extends Scene {
    @Override
    public MainMenu init() {
        reset();
        ImageWidget logo = new ImageWidget("/menu/logo.png", 240, 10).multiplySize(2);
        ClickableImageWidget play = new ClickableImageWidget("/menu/play.png", 370, 175).multiplySize(2).setOnClick((o) -> {
            // start game logic
            GloomGame.getInstance().setScene(2);
        });
        ClickableImageWidget options = new ClickableImageWidget("/menu/options.png", 370, 250).multiplySize(2).setOnClick((o) -> {
            // open options
            GloomGame.getInstance().setScene(1);
        });
        ClickableImageWidget exit = new ClickableImageWidget("/menu/quit.png", 370, 325).multiplySize(2).setOnClick((o) -> {
            // exit game
            System.exit(0);
        });

        ButtonWidget test = new ButtonWidget(370, 400, (b) -> System.out.println("test"), (buttonWidgetGraphicsPair -> {
            buttonWidgetGraphicsPair.second().setColor(Color.cyan);
            buttonWidgetGraphicsPair.second().fillRect(0, 0, 100, 100);
        }), "Play").multiplySize(2);

        add(logo);
        add(play);
        add(options);
        add(exit);
        add(test);
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

        if(Mouse.getInstance().isButtonLeftDown()){
            for (Drawable d : getOfType(ClickableImageWidget.class)) {
                if(WidgetUtil.isMouseOver((GameObject) d)){
                    ClickableImageWidget ciw = (ClickableImageWidget) d;
                    ciw.onInteract(ciw);
                }
            }
        }
    }
}
