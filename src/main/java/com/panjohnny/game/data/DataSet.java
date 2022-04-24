package com.panjohnny.game.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record DataSet(JsonObject data) {

    public String getString(String key) {
        return data.get(key).getAsString();
    }

    public int getInt(String key) {
        return data.get(key).getAsInt();
    }

    public float getFloat(String key) {
        return data.get(key).getAsFloat();
    }

    public double getDouble(String key) {
        return data.get(key).getAsDouble();
    }

    public boolean getBoolean(String key) {
        return data.get(key).getAsBoolean();
    }

    public JsonObject getObject(String key) {
        return data.getAsJsonObject(key);
    }

    public void consume(String key, Consumer<JsonElement> consumer) {
        consumer.accept(data.get(key));
    }

    public Map<String, JsonElement> toMap() {
        return data.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void consumeAll(Consumer<Map.Entry<String, JsonElement>> consumer) {
        data.entrySet().forEach(consumer);
    }

    public boolean isEmpty() {
        return data.entrySet().isEmpty();
    }

    public boolean containsKey(String key) {
        return data.has(key);
    }
}
