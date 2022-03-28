package com.panjohnny.game.data;

import com.panjohnny.game.Options;

import java.util.HashMap;

public class FlagChecker {
    private static final HashMap<String, Runnable> FLAGS = new HashMap<>();

    static {
        FLAGS.put("-dev", Options::setDev);
    }

    public static void check(String[] args) {
        for (String flag : args) {
            if (flag.startsWith("-")) {
                FLAGS.get(flag).run();
            }
        }
    }
}
