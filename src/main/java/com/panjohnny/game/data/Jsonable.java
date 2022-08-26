package com.panjohnny.game.data;

import com.google.gson.JsonElement;

/**
 * Annotation to convert objects into json and push json data back. Used tightly.
 *
 * @apiNote Utility
 */
public interface Jsonable {
    JsonElement toJson();

    void pushJson(JsonElement json);
}
