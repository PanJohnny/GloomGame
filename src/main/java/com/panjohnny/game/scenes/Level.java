package com.panjohnny.game.scenes;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

@Builder(builderClassName = "LevelBuilder")
public class Level extends Scene{
    private final Consumer<Level> init;
    @Getter
    private final String name, author;
    public Level(Consumer<Level> init, String name, String author) {
        this.init = init;
        this.name = name;
        this.author = author;
    }
    @Override
    public Scene init() {
        reset();
        init.accept(this);
        return this;
    }
}
