package com.panjohnny.game.widgets;

import com.panjohnny.game.data.Translator;
import com.panjohnny.game.data.plf.PLFAccess;
import com.panjohnny.game.render.FontRenderer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public class TextWidget extends Widget {
    private String text;
    private int size;
    private Color color;

    public TextWidget(int x, int y, String text, Color color, int size) {
        super(x, y, 0, 0);

        this.color = color;
        this.size = size;
        this.text = text;
    }

    @PLFAccess(mode = PLFAccess.AccessMode.CUSTOM_GETTERS, specialGetterPrefixes = {"", "", "", "get↑", ""}, paramNames = {"x", "y", "text", "rgb", "size"})
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

    public int getRGB() {
        return color.getRGB();
    }
}
