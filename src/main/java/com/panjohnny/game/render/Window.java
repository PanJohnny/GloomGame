package com.panjohnny.game.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.panjohnny.game.data.Jsonable;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class Window implements Jsonable {
    @Getter
    private final JFrame frame;

    public static final String TITLE = "Gloom";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static final boolean MAXIMIZED = false;

    public Window() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WIDTH / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - HEIGHT / 2);
        frame.add(Renderer.getInstance());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setBackground(Colors.DARK);
    }

    public void show() {
        frame.setVisible(true);
    }

    public Point transformPos(int x, int y) {
        float multiplier = ((frame.getWidth() / (float) Window.WIDTH) + (frame.getHeight() / (float) Window.HEIGHT)) / 2f;
        return new Point((int) (x * multiplier), (int) (y * multiplier));
    }

    public Point transformPos(Point p) {
        return transformPos(p.x, p.y);
    }

    public Dimension transformSize(int width, int height) {
        float multiplier = ((frame.getWidth() / (float) Window.WIDTH) + (frame.getHeight() / (float) Window.HEIGHT)) / 2f;
        return new Dimension((int) (width *  multiplier), (int) (height *  multiplier));
    }

    public Dimension transformSize(Dimension d) {
        return transformSize(d.width, d.height);
    }

    public int getWidth() {
        return frame.getWidth();
    }

    public int getHeight() {
        return frame.getHeight();
    }

    public void setSize(int width, int height) {
        frame.setSize(width, height);
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
        frame.setTitle(TITLE + " - " + s);
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
}
