package com.panjohnny.game.render;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontRenderer {
    private static Font font;
    public static void load() {
        // load gloom_original using stream
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderer.class.getResourceAsStream("/assets/fonts/gloom_original.ttf")));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void draw(String text, int size, int x, int y, Color color, Graphics graphics) {
        graphics.setFont(font.deriveFont(Font.PLAIN, size));
        graphics.setColor(color);
        graphics.drawString(text.toUpperCase(), x, y);
    }

    public static void drawCenteredText(String text, int size, int x, int y, Color color, Graphics graphics, Dimension d) {
        graphics.setFont(font.deriveFont(Font.PLAIN, size));
        graphics.setColor(color);
        graphics.drawString(text.toUpperCase(), x + (d.width / 2 - graphics.getFontMetrics().stringWidth(text) / 2) - d.width / 20, graphics.getFontMetrics().getHeight() / 2 + y + d.height/2);
    }
}