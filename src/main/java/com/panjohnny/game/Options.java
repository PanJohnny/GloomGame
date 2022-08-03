package com.panjohnny.game;

import com.google.gson.JsonObject;

public class Options {
    public static final int MAX_FPS = 60;

    public static boolean DEVELOPER_MODE = false;
    public static boolean LEVEL_DESIGNER = false;

    public static boolean UNIT_TESTING_MODE = false;

    //TODO make this a configurable option and working
    public static float volume = 0.1f;

    public static void setDev() {
        DEVELOPER_MODE = true;
    }

    public static void setLevel() {
        LEVEL_DESIGNER = true;
    }

    public static void setOnlyInit() {
        UNIT_TESTING_MODE = true;
    }

    public static JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("volume", volume);
        return json;
    }

    public static void load(JsonObject json) {
        volume = json.get("volume").getAsFloat();
    }
}
