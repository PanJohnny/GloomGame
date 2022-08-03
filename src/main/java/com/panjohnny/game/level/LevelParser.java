package com.panjohnny.game.level;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.plf.PLFPrefabs;
import com.panjohnny.game.data.plf.PLFTools;
import com.panjohnny.game.mem.AssetNotFoundException;
import com.panjohnny.game.render.Drawable;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;

public final class LevelParser {
    private static final double CURRENT_VERSION = 0.1;

    @SneakyThrows
    public static Level parseInternal(String file) {
        // load internal resource as stream
        InputStream inputStream = LevelParser.class.getResourceAsStream("/assets/levels/" + file);
        // load internal resource as string
        if (inputStream == null) {
            throw new AssetNotFoundException(file);
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder sb = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            sb.append(line).append("\n");
        }
        String asString = sb.toString();
        reader.close();
        return load(asString);
    }

    public static Level parseExternal(String file) {
        return load(GloomGame.getInstance().getDataManager().loadFile("/assets/levels/" + file).getAsString());
    }

    private static Level load(String asString) {
        String[] lines = asString.split("\n");
        String version = lines[0];

        // check if version is double
        if (!version.matches("^\\d+(\\.\\d+)?$")) {
            throw new IllegalArgumentException("Invalid version: " + version);
        }

        if (version.equals(String.valueOf(CURRENT_VERSION))) {
            return loadCurrent(lines);
        } else {
            throw new DeprecatedLevelFileVersionException(version);
        }
    }

    private static Level loadCurrent(String[] lines) {
        Level.LevelBuilder builder = Level.builder();

        builder.name(lines[1]);
        builder.author(lines[2]);
        String[] objects = lines[3].replaceAll("[\\[\\]]", "").split(";;");
        String[] prefabs = lines[4].replaceAll("[\\[\\]]", "").split(";;");
        builder.init(createInit(objects, prefabs));

        System.out.println("[LEVEL PARSER] Level loaded: " + builder);

        return builder.build();
    }

    private static Consumer<Level> createInit(String[] objects, String[] prefabs) {
        return (l) -> {
            System.out.println("[LEVEL PARSER] Level init: " + Arrays.toString(objects));
            for (String o :
                    objects) {
                try {
                    Object ob = PLFTools.convertString(o);
                    if (ob instanceof GameObject) {
                        l.add((GameObject) ob);
                    } else if (ob instanceof Drawable) {
                        l.addDrawable((Drawable) ob);
                    } else
                        System.err.println("[LEVEL PARSER] Unknown object: " + ob + " (" + o + ")");
                    System.out.println("Handled init for: " + o);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         ClassNotFoundException | NoSuchMethodException e) {
                    System.err.println("Failed to load object: " + o);
                    throw new RuntimeException(e);
                }
            }
            for (String p :
                    prefabs) {
                PLFPrefabs.invokeSafe(p, l);
            }
        };
    }

    public static class DeprecatedLevelFileVersionException extends RuntimeException {
        public DeprecatedLevelFileVersionException(String version) {
            super("Deprecated level file version: " + version + " (current version: " + CURRENT_VERSION + ")");
        }
    }
}
