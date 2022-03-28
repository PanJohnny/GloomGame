package com.panjohnny.game.widgets;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.render.FontRenderer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public class TextWidget extends GameObject {
    private String text;
    private int size;
    private Color color;
    public TextWidget(int x, int y, String text, Color color, int size) {
        super(x, y, 0, 0);

        this.color = color;
        this.size = size;
        this.text = text;
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw(Graphics g) {
        Point p = getActualPosition();
        FontRenderer.draw(text, size, p.x, p.y, color, g);
    }
}