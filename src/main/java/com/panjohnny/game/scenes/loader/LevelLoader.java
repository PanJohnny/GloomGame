package com.panjohnny.game.scenes.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.scenes.Level;
import com.panjohnny.game.tile.Tile;
import com.panjohnny.game.util.Factory;

import java.util.HashMap;
import java.util.LinkedList;

public final class LevelLoader {
    private static final HashMap<String, Level> levels = new HashMap<>();
    private static final HashMap<String, Factory<GameObject>> TYPES = new HashMap<>();

    static {
        TYPES.put("Tile", Tile::new);
    }
    public static Level loadLevel(String file) {
        return load(GloomGame.getInstance().getDataFetcher().getAsJsonObject("levels/" + file + ".json"));
    }

    private static Level load(JsonObject json) {
        LinkedList<GameObject> list = new LinkedList<>();
        String name = json.get("name").getAsString();
        String id = json.get("id").getAsString();
        String author = json.get("author").getAsString();
        JsonArray objects = json.get("objects").getAsJsonArray();
        JsonObject levelData = json.get("levelData").getAsJsonObject();

        objects.forEach(object -> {
            JsonObject objectData = object.getAsJsonObject();
            String type = objectData.get("type").getAsString();
            int x = objectData.get("x").getAsInt();
            int y = objectData.get("y").getAsInt();
            int width, height;
            if(!type.startsWith("#")) {
                width = objectData.get("width").getAsInt();
                height = objectData.get("height").getAsInt();
            } else {
                width = 0;
                height = 0;
            }

            if(TYPES.containsKey(type)) {
                GameObject gameObject = TYPES.get(type).create();
                gameObject.apply((o) -> {
                    o.setX(x);
                    o.setY(y);
                    o.setWidth(width);
                    o.setHeight(height);
                });

                list.add(gameObject);
            }
        });

        System.out.println(list);
        return null;
    }
}
