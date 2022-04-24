package com.panjohnny.game.data;

import com.google.gson.JsonObject;
import com.panjohnny.game.GloomGame;

public final class Translator {
    private static final JsonObject DEFAULT = GloomGame.getInstance().getDataFetcher().get("/lang/en.json").getAsJsonObject();

    private static JsonObject currentLang = DEFAULT;
    public static String translate(String key) {
        if(!currentLang.has(key)) {
            return DEFAULT.get(key).getAsString();
        }
        return currentLang.get(key).getAsString();
    }

    public static void setLanguage(String lang) {
        currentLang = GloomGame.getInstance().getDataFetcher().get("/lang/" + lang + ".json").getAsJsonObject();
    }

    public static void loadOuterLanguage(String lang) {
        DataSet set = GloomGame.getInstance().getDataManager().loadFile("/lang/" + lang + ".json)");
        if(!validate(set)) {
            System.err.println("Invalid language file: " + lang);
        }
        currentLang = set.data();
    }

    public static boolean validate(DataSet set) {
        return set.containsKey("name") && set.containsKey("version") && set.containsKey("authors") && set.containsKey("code");
    }
}
