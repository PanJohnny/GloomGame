package com.panjohnny.game.level;

import com.panjohnny.game.GameObject;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    private final LevelDesigner levelDesigner;

    public ObjectOptionWidget(List<Option<?>> options, int x, int y, int optionHeight, LevelDesigner levelDesigner) {
        super("/assets/designer/options/header.png", 128, 0, x, y);
        this.options = options;

        background = GloomGame.getInstance().getImageFetcher().get("/assets/designer/background.png");
        this.optionHeight = optionHeight;
        this.levelDesigner = levelDesigner;
    }

    public Option<?> get(String name) {
        for (Option<?> option : options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        return null;
    }

    public Object getValue(String name) {
        Option<?> option = get(name);
        if (option != null) {
            return option.getValue();
        }
        return null;
    }

    @Override
    public void draw(Graphics g) {
        if(options.size() == 0 || levelDesigner.getSelected() == null || !levelDesigner.isInEditMode()) {
            return;
        }
        super.draw(g);
        FontRenderer.draw("Options", 15, getX()+5, getY() + getHeight() - 2, Colors.DARK, g);
        g.drawImage(background, getX(), getY() + getHeight(), getWidth(), options.size() * optionHeight, null);
        // go through the options and draw them
        for (int i = 0; i < options.size(); i++) {
            Option<?> option = options.get(i);
            FontRenderer.draw(option.toString(), 15, getX() + 5, getY() + getHeight() * 2 + i * optionHeight, Colors.DARK, g);
        }
    }

    @Override
    public void onInteract(MouseClickEvent context) {
        System.out.println("Interacting with object options");
        for (int i = 0; i < options.size(); i++) {
            Option<?> option = options.get(i);
            Rectangle bound = new Rectangle(getX(), getY() + getHeight() + i * optionHeight, getWidth(), optionHeight);
            if (bound.intersects(Mouse.getInstance().getFixedBounds())) {
                if (context.isLeftClick())
                    option.prompt();
                else if (context.isRightClick())
                    option.delete();
                else if (context.isMiddleClick())
                    option.debug();
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

        public static Option<String> string(String name, String value, Consumer<Option<String>> consumer) {
            return new Option<>(name, value, consumer, String::valueOf);
        }

        public static <T> Consumer<Option<T>> emptyConsumer() {
            return option -> {
            };
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
                    JOptionPane.showMessageDialog(null, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public void delete() {
            update(converter.apply(value instanceof Number ? "0" : ""));
        }

        public void debug() {
            JOptionPane.showMessageDialog(null, "The value is: %s".formatted(value), "Value of %s".formatted(name), JOptionPane.INFORMATION_MESSAGE);
        }

        public String toString() {
            String s = name + ": " + value;
            if (s.length() >= 15) {
                s = s.substring(0, 12) + "...";
            }
            return s;
        }
    }
}
