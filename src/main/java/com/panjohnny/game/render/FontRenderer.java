package com.panjohnny.game.render;

import com.panjohnny.game.GloomGame;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class FontRenderer {
    private static Font font;
    public static void load() {
        // load gloom_original using stream
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderer.class.getResourceAsStream("/gloom_original.ttf")));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void draw(String text, int size, int x, int y, Color color, Graphics graphics) {
        graphics.setFont(font.deriveFont(Font.PLAIN, size));
        graphics.setColor(color);
        graphics.drawString(text.toUpperCase(), x, y);
    }

    public static int getWidth(String text, int size) {
        return (int) (text.length() * size * 0.6);
    }

    public static void drawCenteredText(String text, int size, int x, int y, Color color, Graphics graphics, Dimension d) {
        graphics.setFont(font.deriveFont(Font.PLAIN, size));
        graphics.setColor(color);
        graphics.drawString(text.toUpperCase(), (int) ((x + d.width/2) - GloomGame.getInstance().getWindow().multiply(text.length()) * 2), y + size + d.height / 5);
    }
}
