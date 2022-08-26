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
        FLAGS.put("-dev", () -> Options.set(Options.Option.DEVELOPER_MODE, true));
        FLAGS.put("-level", () -> Options.set(Options.Option.LEVEL_DESIGNER, true));
        FLAGS.put("-only-init", () -> Options.set(Options.Option.UNIT_TESTING, true));
    }

    /**
     * Used to check for flags in {@link com.panjohnny.game.GloomGame#main(String[])}
     *
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
