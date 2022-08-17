package com.panjohnny.game.data;

import com.panjohnny.game.Options;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Used to check for flags in {@link com.panjohnny.game.GloomGame#main(String[])}
 */
public class FlagChecker {
    private static final HashMap<String, Runnable> FLAGS = new HashMap<>();
    public static List<String> flags;

    static {
        FLAGS.put("-dev", Options::setDev);
        FLAGS.put("-level", Options::setLevel);
        FLAGS.put("-only-init", Options::setOnlyInit);
    }

    /**
     * Used to check for flags in {@link com.panjohnny.game.GloomGame#main(String[])}
     * @param args String args of the main method
     * @see com.panjohnny.game.GloomGame#main(String[])
     */
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
