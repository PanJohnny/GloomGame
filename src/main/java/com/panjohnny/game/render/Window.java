package com.panjohnny.game.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.panjohnny.game.data.Jsonable;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Window implements Jsonable {
    @Getter
    private final JFrame frame;

    public static final String TITLE = "Gloom";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;

    public static final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
    public static final Cursor DEFAULT_CURSOR;

    @Setter
    private String staticTitleAppend = "";

    static {
        try {
            DEFAULT_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Objects.requireNonNull(Window.class.getResourceAsStream("/assets/cursor.png"))), new Point(0, 0), "default cursor");
        } catch (IOException e) {
            System.err.println("Failed to load default cursor");
            throw new RuntimeException(e);
        }
    }

    public Window() {
        frame = new JFrame(TITLE);
        // TODO lock aspect ratio
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                Rectangle b = arg0.getComponent().getBounds();
                arg0.getComponent().setBounds(b.x, b.y, b.width, b.width*HEIGHT/WIDTH);
                System.out.println(arg0.paramString());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT / 2);
        frame.add(Renderer.getInstance());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setBackground(Colors.DARK);

        showCursor(true);
    }

    public void show() {
        frame.setVisible(true);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public boolean isMaximized() {
        return frame.getExtendedState() == JFrame.MAXIMIZED_BOTH;
    }

    public void setMaximized(boolean maximized) {
        if (maximized) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(JFrame.NORMAL);
        }
    }

    public void debug(String s) {
        frame.setTitle(TITLE + " - " + s + " // " + staticTitleAppend);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("maximized", isMaximized());
        json.addProperty("posX", frame.getX());
        json.addProperty("posY", frame.getY());

        return json;
    }

    @Override
    public void pushJson(JsonElement json) {
        JsonObject object = json.getAsJsonObject();

        if (object.has("maximized")) {
            setMaximized(object.get("maximized").getAsBoolean());
        }

        if (object.has("posX") && object.has("posY")) {
            frame.setLocation(object.get("posX").getAsInt(), object.get("posY").getAsInt());
        }
    }

    public float multiply(float original) {
        return original * ((frame.getWidth() / (float) Window.WIDTH) + (frame.getHeight() / (float) Window.HEIGHT)) / 2f;
    }

    public void showCursor(boolean show) {
        frame.setCursor(show ? DEFAULT_CURSOR : BLANK_CURSOR);
    }

    public void setCustomCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }
}
