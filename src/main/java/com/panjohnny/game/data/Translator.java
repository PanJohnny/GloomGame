package com.panjohnny.game.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.panjohnny.game.GloomGame;

/**
 * Class used to translate files.
 * <h1>Translation format</h1>
 * <p>
 * Translation files do have their format. This is a example<code>
 * <pre>
 * {
 *   "name": "American English",
 *   "code": "en_us",
 *   "authors": [
 *     "PanJohnny"
 *   ],
 *   "version": "alpha 1.0",
 *   "menu.options": "Options",
 *   "btn.play": "Play",
 *   "btn.options": "Options",
 *   "btn.quit": "Quit",
 *   "btn.back": "Back",
 *   "options.volume": "Volume",
 *   "dev.info": "A lot of stuff may change, some aspects may not work at all. This is a development build.",
 *   "menu.loading": "Loading..."
 * }
 *         </pre>
 * </code>
 * First you will provide some basic information and then you add the keys.
 * NOTE: version is on the person who creates the file. This feature might get removed.
 * </p>
 *
 * @implNote There is a problem with font file not supporting non-basic characters. I need to make new font.
 */
public final class Translator {
    /**
     * The default lang file. (EN_US)
     */
    private static final JsonObject DEFAULT = GloomGame.getInstance().getDataFetcher().get("/assets/lang/en_us.json").getAsJsonObject();

    /**
     * Current lang used by the translator.
     */
    private static JsonObject currentLang = DEFAULT;

    /**
     * Used to translate key to string.
     *
     * @param key Key to get
     * @return If no key is found the key would be found it would try with the default language, then key will be returned.
     */
    public static String translate(String key) {
        if (!currentLang.has(key)) {
            if (!DEFAULT.has(key)) {
                System.err.println("Missing translation for key: " + key);
                return key;
            }
            return DEFAULT.get(key).getAsString();
        }
        return currentLang.get(key).getAsString();
    }

    /**
     * Fetches inner language.
     *
     * @param lang Name of the language file (e. g. en_us)
     */
    public static void setLanguage(String lang) {
        currentLang = GloomGame.getInstance().getDataFetcher().get("/assets/lang/" + lang + ".json").getAsJsonObject();
    }

    /**
     * Loads outer lang file (from <code>./data/assets/lang/{lang}.json</code>
     *
     * @param lang The name of the lang file.
     */
    public static void loadOuterLanguage(String lang) {
        JsonElement set = GloomGame.getInstance().getDataManager().loadFile("/assets/lang/" + lang + ".json");
        if (!validate(set.getAsJsonObject())) {
            System.err.println("Invalid language file: " + lang);
        }
        currentLang = set.getAsJsonObject();
    }

    /**
     * Validates the JsonObject if it contains mandatory values and is not a null.
     *
     * @param set The object to validate
     * @return True if object is valid
     */
    public static boolean validate(JsonObject set) {
        return set != null && set.has("name") && set.has("version") && set.has("authors") && set.has("code");
    }

    /**
     * @apiNote Used for inner stuff
     * @hidden
     */
    public static JsonElement toJson() {
        return currentLang.get("code") == null ? DEFAULT.get("code") : currentLang.get("code");
    }

    /**
     * Default way to load the file. If there is no inner outer is loaded.
     *
     * @param lang The name of the lang file.
     */
    public static void load(String lang) {
        if (lang == null) {
            return;
        }
        // look if outer exists
        if (GameDataManager.exists("/assets/lang/" + lang + ".json")) {
            loadOuterLanguage(lang);
        } else {
            if (GloomGame.getInstance().getDataFetcher().exists("/assets/lang/" + lang + ".json")) {
                setLanguage(lang);
            } else {
                System.err.println("Error, failed to load language: " + lang + ", not found");
            }
        }
    }
}
