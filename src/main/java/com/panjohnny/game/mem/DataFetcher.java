package com.panjohnny.game.mem;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Used to fetch local data.
 *
 * @implNote It is only JsonElements which sucks...
 */
public class DataFetcher {

    // TODO make better...

    private final LoadingCache<String, JsonElement> cache;

    public DataFetcher() {
        CacheLoader<String, JsonElement> loader = new CacheLoader<>() {
            @Override
            @NonNull
            public JsonElement load(@NonNull String path) {
                InputStream stream = DataFetcher.class.getResourceAsStream(path);
                if (stream == null) {
                    throw new AssetNotFoundException(path);
                } else {
                    return JsonParser.parseReader(new InputStreamReader(stream));
                }
            }
        };

        this.cache = CacheBuilder.newBuilder().build(loader);
    }

    public JsonElement get(String path) {
        return cache.getUnchecked(path);
    }

    public JsonObject getAsJsonObject(String path) {
        return get(path).getAsJsonObject();
    }

    public JsonArray getAsJsonArray(String path) {
        return get(path).getAsJsonArray();
    }

    public long size() {
        return cache.size();
    }

    public void invalidate(String path) {
        cache.invalidate(path);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void consume(Consumer<JsonElement> elementConsumer, String path) {
        elementConsumer.accept(get(path));
    }

    public LoadingCache<String, JsonElement> getCache() {
        return cache;
    }

    public boolean exists(String path) {
        try {
            get(path);
            return true;
        } catch (AssetNotFoundException e) {
            return false;
        }
    }
}
