package com.panjohnny.game.scenes;

import com.panjohnny.game.GloomGame;
import lombok.Getter;

public class FiledScene extends Scene {
    @Getter
    private final String name;
    public FiledScene(String name) {
        this.name = name;
    }
    @Override
    public FiledScene init() {
        // load the scene from inner file in /scenes/name.json
        GloomGame.getInstance().getDataFetcher().get("/scenes/%s.json".formatted(this.name));
        return this;
    }

    @Override
    public void onKeyPress(int key) {

    }

    @Override
    public void onKeyRelease(int key) {

    }
}
