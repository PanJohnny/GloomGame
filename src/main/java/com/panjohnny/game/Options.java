package com.panjohnny.game;

import com.google.gson.JsonObject;
import com.panjohnny.game.io.SoundPlayer;

import java.util.HashMap;
import java.util.function.Consumer;

public class Options {

    private static final HashMap<String, Object> OPTIONS_CONTAINER = new HashMap<>();

    static {
        for (Option option : Option.values()) {
            OPTIONS_CONTAINER.put(option.key, option.defaultValue);
        }
    }

    public static <T> T get(Option option, Class<T> tClass) {
        if (!option.defaultValue.getClass().isAssignableFrom(tClass))
            throw new ClassCastException("Cannot cast " + option.defaultValue.getClass() + " to " + tClass);

        return tClass.cast(OPTIONS_CONTAINER.get(option.key));
    }

    public static boolean getBoolean(Option option) {
        return get(option, Boolean.class);
    }

    public static float getFloat(Option option) {
        return get(option, Float.class);
    }

    public static int getInt(Option option) {
        return get(option, Integer.class);
    }

    public static void set(Option option, Object value) {
        if (!option.defaultValue.getClass().isAssignableFrom(value.getClass()))
            throw new ClassCastException("Cannot cast " + option.defaultValue.getClass() + " to " + value.getClass());

        OPTIONS_CONTAINER.put(option.key, value);
        option.update();
    }

    public static JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("volume", getFloat(Option.VOLUME));
        return json;
    }

    public static void load(JsonObject json) {
        set(Option.VOLUME, json.get("volume").getAsFloat());
    }

    public enum Option {
        MAX_FPS("max_fps", 60),
        LEVEL_DESIGNER("level_designer", false),
        DEVELOPER_MODE("developer_mode", false),
        UNIT_TESTING("unit_testing", false),
        VOLUME("volume", 0.5f, (self) -> SoundPlayer.setVolume(Options.getFloat(self)));

        final String key;
        final Object defaultValue;
        Consumer<Option> onUpdate;

        Option(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        Option(String key, Object defaultValue, Consumer<Option> onUpdate) {
            this(key, defaultValue);
            this.onUpdate = onUpdate;
        }

        void update() {
            if (onUpdate == null)
                return;
            onUpdate.accept(this);
        }
    }

}
