package com.panjohnny.game.widgets;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.FontRenderer;
import com.panjohnny.game.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Consumer;

public class ButtonWidget extends ClickableImageWidget{
    private final Consumer<Pair<ButtonWidget, Graphics>> onHover;
    @Getter
    @Setter
    private String text;
    public ButtonWidget(int x, int y, Consumer<ButtonWidget> onClick, Consumer<Pair<ButtonWidget, Graphics>> onHover, String text) {
        super(GloomGame.getInstance().getImageFetcher().get("/assets/menu/widgets/button.png"),  x, y);

        this.text = text;
        this.onHover = onHover;
        setOnClick(i -> onClick.accept(this));
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        Point p = getActualPosition();
        Dimension d = getActualSize();
        // Draw text centered on the button using FontRenderer
        FontRenderer.drawCenteredText(text, (int) ((d.height / 2) - GloomGame.getInstance().getWindow().multiply(text.length() * .5f)), p.x, p.y, Colors.YELLOW, g, d);
        if(WidgetUtil.isMouseOver(this)) {
            onHover.accept(new Pair<>(this, g));
        }
    }

    @Override
    public ButtonWidget multiplySize(double scale) {
        return (ButtonWidget) super.multiplySize(scale);
    }

    public static void overlay(Pair<ButtonWidget, Graphics> pair, Color color) {
        Graphics g = pair.second();
        ButtonWidget w = pair.first();
        g.setColor(color);
        g.fillRect(w.getActualPosition().x, w.getActualPosition().y, w.getActualSize().width, w.getActualSize().height);
    }
}
