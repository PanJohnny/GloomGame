package com.panjohnny.game.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.panjohnny.game.GloomGame;

public final class Translator {
    private static final JsonObject DEFAULT = GloomGame.getInstance().getDataFetcher().get("/assets/lang/en_us.json").getAsJsonObject();

    private static JsonObject currentLang = DEFAULT;
    public static String translate(String key) {
        if(!currentLang.has(key)) {
            if(!DEFAULT.has(key)) {
                System.err.println("Missing translation for key: " + key);
                return key;
            }
            return DEFAULT.get(key).getAsString();
        }
        return currentLang.get(key).getAsString();
    }

    public static void setLanguage(String lang) {
        currentLang = GloomGame.getInstance().getDataFetcher().get("/assets/lang/" + lang + ".json").getAsJsonObject();
    }

    public static void loadOuterLanguage(String lang) {
        DataSet set = GloomGame.getInstance().getDataManager().loadFile("/assets/lang/" + lang + ".json");
        if(!validate(set)) {
            System.err.println("Invalid language file: " + lang);
        }
        currentLang = set.data();
    }

    public static boolean validate(DataSet set) {
        return set!=null && set.data()!=null && set.containsKey("name") && set.containsKey("version") && set.containsKey("authors") && set.containsKey("code");
    }

    public static JsonElement toJson() {
        return currentLang.get("code") == null? DEFAULT.get("code") : currentLang.get("code");
    }

    public static void load(String lang) {
        if(lang == null) {
            return;
        }
        // look if outer exists
        if(GameDataManager.exists("/assets/lang/" + lang + ".json")) {
            loadOuterLanguage(lang);
        } else {
            if(GloomGame.getInstance().getDataFetcher().exists("/assets/lang/" + lang + ".json")) {
                setLanguage(lang);
            } else {
                System.err.println("Error, failed to load language: " + lang + ", not found");
            }
        }
    }
}
