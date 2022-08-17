package com.panjohnny.game.data.plf;

import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.level.Level;
import com.panjohnny.game.light.BakedLights;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.widgets.ButtonWidget;

import java.awt.*;
import java.util.HashMap;

/**
 * This class is used for creating levels. I implemented the prefab system because I was to lazy to do something else. In the plf file you have list of prefabs
 * that you can use to create your level. For example music(level_001) will set that the level should play music named level_001.
 */
public final class PLFPrefabs {
    public static final HashMap<String, Prefab> PREFABS = new HashMap<>();

    static {
        PREFABS.put("addp", (l, args) -> {
            GloomGame.getInstance().getPlayer().setX(Integer.parseInt(args[0]));
            GloomGame.getInstance().getPlayer().setY(Integer.parseInt(args[1]));
            l.add(GloomGame.getInstance().getPlayer());
        });

        PREFABS.put("overlay", (l, args) -> {
            // adds an overlay to the level
            l.add(new ButtonWidget(600, 20, (b) -> GloomGame.getInstance().setScene(0), pair -> ButtonWidget.overlay(pair, Colors.alpha(Colors.RED, 0.2f)), Translator.translate("btn.back")).multiplySize(2));
        });

        PREFABS.put("events", (l, args) -> GloomGame.registerEventListener(l));

        /*
        *** LIGHTS ***
         */
        PREFABS.put("bl-round", (l, args) -> {
            // adds rounded light to the level
            // args[0] = x
            // args[1] = y
            // args[2] = radius
            // args[3] = color

            l.add(BakedLights.createRounded(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), new Color(Integer.parseInt(args[3]))));
        });
        PREFABS.put("bl-rec", (l, args) -> {
            // adds rectangle light to the level
            // args[0] = x
            // args[1] = y
            // args[2] = width
            // args[3] = height
            // args[4] = color

            l.add(BakedLights.createRectangle(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), new Color(Integer.parseInt(args[4]))));
        });
        PREFABS.put("bl-tri", (l, args) -> {
            // adds triangular light to the level
            // args[0] = x
            // args[1] = y
            // args[2] = width
            // args[3] = height
            // args[4] = color

            l.add(BakedLights.createTriangular(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), new Color(Integer.parseInt(args[4]))));
        });

        PREFABS.put("music", (l, args) -> {
           l.setSong(args[0]);
        });
    }

    public static void invokeSafe(String str, Level l) {
        // the string looks like this: name(arg1, arg2, ...)
        String[] split = str.split("\\(");
        String name = split[0];
        String[] args = split[1].substring(0, split[1].length() - 1).split(",");

        Prefab prefab = PREFABS.get(name);
        if (prefab == null) {
            System.err.println("[PREFABS] Unknown prefab: " + name);
            return;
        }

        try {
            prefab.init(l, args);
        } catch (Exception e) {
            System.err.println("[PREFABS] Error while invoking prefab " + name);
            e.printStackTrace();
            System.err.println("-----------------------------------------------------");
        }
    }

    public interface Prefab {
        void init(Level level, String[] args);
    }
}
