package com.panjohnny.game.scenes;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.render.Renderer;
import com.panjohnny.game.widgets.InteractionWidget;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public abstract class Scene {
    @Getter
    private final LinkedList<Drawable> drawables = new LinkedList<>();
    @Getter
    private final LinkedList<GameObject> objects = new LinkedList<>();

    public abstract Scene init();

    public void reset() {
        objects.clear();
        drawables.clear();
    }

    public void add(GameObject object) {
        objects.add(object);
        drawables.add(object);

        System.out.println("Added object: " + object.getClass().getSimpleName());
    }

    public void remove(GameObject object) {
        objects.remove(object);
    }

    public void update() {
        for (GameObject object : objects) {
            object.tick();
        }
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    public void render() {
        Renderer.getInstance().render(drawables);
    }

    public abstract void onKeyPress(int key);
    public abstract void onKeyRelease(int key);

    public List<Drawable> getOfType(Class<?> type) {
        List<Drawable> result = new LinkedList<>();
        for (Drawable drawable : drawables) {
            if (type.isInstance(drawable)) {
                result.add(drawable);
            }
        }
        return result;
    }
}
