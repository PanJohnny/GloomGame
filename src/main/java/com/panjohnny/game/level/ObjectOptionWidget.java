package com.panjohnny.game.level;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.FontRenderer;
import com.panjohnny.game.widgets.ImageWidget;
import com.panjohnny.game.widgets.InteractionWidget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectOptionWidget extends ImageWidget implements InteractionWidget<MouseClickEvent> {
    @Setter
    private List<Option<?>> options;
    private final BufferedImage background;

    private final int optionHeight;

    public ObjectOptionWidget(List<Option<?>> options, int x, int y, int optionHeight) {
        super("/assets/designer/options/header.png", 128, 0, x, y);
        this.options = options;

        background = GloomGame.getInstance().getImageFetcher().get("/assets/designer/background.png");
        this.optionHeight = optionHeight;
    }

    public Option<?> get(String name) {
        for (Option<?> option : options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(background, getX(), getY() + getHeight(), getWidth(), options.size() * optionHeight, null);
        // go through the options and draw them
        for (int i = 0; i < options.size(); i++) {
            Option<?> option = options.get(i);
            FontRenderer.draw(option.name + ": " + option.value, 15, getX() + 5, getY() + getHeight() * 2 + i * optionHeight, Colors.DARK, g);
        }
    }

    @Override
    public void onInteract(MouseClickEvent context) {
        System.out.println("clicked");
        for (int i = 0; i < options.size(); i++) {
            Option<?> option = options.get(i);
            Rectangle bound = new Rectangle(getX(), getY() + getHeight() + i * optionHeight, getWidth(), optionHeight);
            if (bound.intersects(Mouse.getInstance().getFixedBounds())) {
                option.prompt();
            }
        }
    }

    public boolean isOver(int x, int y) {
        return Mouse.getInstance().getFixedBounds().intersects(new Rectangle(getX(), getY() + getHeight(), getWidth(), options.size() * optionHeight));
    }

    public void set(int i, Option<?> option) {
        options.set(i, option);
    }

    public void clear() {
        options.clear();
    }


    @AllArgsConstructor
    @Getter
    public static class Option<T> {

        public static Option<Integer> integer(String name, int value, Consumer<Option<Integer>> consumer) {
            return new Option<>(name, value, consumer, Integer::parseInt);
        }

        private final String name;
        private T value;
        private final Consumer<Option<T>> onUpdate;
        private final Function<String, T> converter;

        public void update(T t) {
            value = t;
            onUpdate.accept(this);
        }

        public void prompt() {
            // prompt for value
            String s = JOptionPane.showInputDialog("Enter value for " + name);
            if (s != null) {
                try {
                    update(converter.apply(s));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid value");
                }
            }
        }
    }
}
