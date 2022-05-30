package com.panjohnny.game.scenes;

import lombok.Getter;

import java.util.function.Consumer;

public class Level extends Scene{
    private final Consumer<Level> init;
    @Getter
    private final String name, author, id;
    public Level(Consumer<Level> init, String name, String author, String id) {
        this.init = init;
        this.name = name;
        this.author = author;
        this.id = id;
    }
    @Override
    public Scene init() {
        reset();
        init.accept(this);
        return this;
    }
}
