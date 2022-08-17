package com.panjohnny.game.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.*;

/**
 * Used to load in and save external files in the ./data folder
 */
public class GameDataManager {

    /**
     * Loads a external file as a JsonElement
     * @param fileName The name to the file. (may include paths)
     */
    public JsonElement loadFile(String fileName) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if (!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }

            return new JsonObject();
        }

        // create FileReader and read it as Json (use Gson)
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file " + file.getAbsolutePath(), e);
        }
        // create DataSet from JsonElement
        return jsonElement;
    }

    /**
     * Saves external file with JsonElement.
     * @param dataSet The JsonElement to save to the file.
     * @param fileName The name to the file. (may include paths)
     */
    public void saveFile(@NonNull JsonElement dataSet, String fileName) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if (!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }
        }

        // create FileWriter and write it as Json (use Gson)
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(dataSet.toString());
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes string to the file
     * @param fileName The name to the file. (may include paths)
     * @param content The string to save.
     */
    public void writeFile(String fileName, String content) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if (!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }
        }

        // create FileWriter and write it as Json (use Gson)
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if file exists in the ./data/ folder.
     * @param path Path to the file
     * @return True if the file exists
     * @see File#exists() 
     */
    public static boolean exists(String path) {
        return new File("data/" + path).exists();
    }
}
