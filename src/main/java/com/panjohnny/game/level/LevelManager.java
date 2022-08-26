package com.panjohnny.game.level;

import com.panjohnny.game.GloomGame;
import lombok.Getter;

import java.util.LinkedList;

/**
 * Used to manage levels.
 */
public class LevelManager {
    private final LinkedList<Level> levels = new LinkedList<>();
    @Getter
    private Level current;

    public LevelManager(Level first) {
        levels.add(first);
    }

    /**
     * Changes the current level to the next one. Used only internally by LoadingScene.
     *
     * @param levelToChange The level to change to.
     */
    public void change(Level levelToChange) {
        if (current != null)
            GloomGame.unregisterEventListener(current);
        current = levelToChange;

        GloomGame.getInstance().setSceneHard(current);
    }

    public void load(int index) {
        if (index < 0 || index >= levels.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        GloomGame.getInstance().setSceneHard(new LoadingScene(levels.get(index), this));
    }

    public void register(Level level) {
        levels.add(level);
    }
}
