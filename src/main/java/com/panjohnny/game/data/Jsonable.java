package com.panjohnny.game.data;

import com.google.gson.JsonElement;

public interface Jsonable {
    JsonElement toJson();
    void pushJson(JsonElement json);
}
