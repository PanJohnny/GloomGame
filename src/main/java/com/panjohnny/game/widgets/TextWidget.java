package com.panjohnny.game.widgets;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.data.plf.AdvancedPLFMember;
import com.panjohnny.game.data.plf.PLFTools;
import com.panjohnny.game.render.FontRenderer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public class TextWidget extends Widget implements AdvancedPLFMember {
    private String text;
    private int size;
    private Color color;
    public TextWidget(int x, int y, String text, Color color, int size) {
        super(x, y, 0, 0);

        this.color = color;
        this.size = size;
        this.text = text;
    }

    public TextWidget(int x, int y, String text, int rgb, int size) {
        this(x, y, text, new Color(rgb), size);
    }

    public static TextWidget translated(int x, int y, String key, Color color, int size) {
        return new TextWidget(x, y, Translator.translate(key), color, size);
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw(Graphics g) {
        Point p = getActualPosition();
        FontRenderer.draw(text, size, p.x, p.y, color, g);
    }

    @Override
    public String convertToString() {
        return PLFTools.genBasicObjectPrepend(this.getClass()) +
                getX() + "," + getY() + "," + PLFTools.makeStringSuitable(getText()) + "," + getColor().getRGB() + "," + getSize() +
                ")";
    }
}
