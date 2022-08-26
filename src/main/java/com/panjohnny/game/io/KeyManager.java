package com.panjohnny.game.io;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public final class KeyManager {
    public static final Collection<Integer> JUMP_KEYS;
    public static final Collection<Integer> MOVE_RIGHT_KEYS;
    public static final Collection<Integer> MOVE_LEFT_KEYS;

    static {
        JUMP_KEYS = List.of(KeyEvent.VK_SPACE, KeyEvent.VK_UP, KeyEvent.VK_W);
        MOVE_RIGHT_KEYS = List.of(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
        MOVE_LEFT_KEYS = List.of(KeyEvent.VK_A, KeyEvent.VK_LEFT);
    }

    /**
     * Checks if the integer is in the collection.
     *
     * @param keys    Collection to check against
     * @param keyCode The code of the key
     * @return True if in fact the collection includes the key.
     * @implNote This could be probably handled better.
     * @see KeyEvent
     */
    public static boolean isOf(Collection<Integer> keys, int keyCode) {
        return keys.contains(keyCode);
    }
}
