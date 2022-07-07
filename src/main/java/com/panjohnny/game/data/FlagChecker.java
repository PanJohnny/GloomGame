package com.panjohnny.game.data;

import com.panjohnny.game.Options;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FlagChecker {
    private static final HashMap<String, Runnable> FLAGS = new HashMap<>();
    public static List<String> flags;

    static {
        FLAGS.put("-dev", Options::setDev);
        FLAGS.put("-level", Options::setLevel);
        FLAGS.put("-only-init", Options::setOnlyInit);
    }

    public static void check(String[] args) {
        flags = new LinkedList<>();
        for (String flag : args) {
            if (flag.startsWith("-")) {
                FLAGS.get(flag).run();
                flags.add(flag);
            }
        }
    }
}
