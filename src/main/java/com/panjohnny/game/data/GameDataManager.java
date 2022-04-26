package com.panjohnny.game.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;

import java.io.*;

public class GameDataManager {

    public GameDataManager() {
    }

    public DataSet loadFile(String fileName) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if(!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if(!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if(!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }

            return createEmpty();
        }

        // create FileReader and read it as Json (use Gson)
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file " + file.getAbsolutePath(), e);
        }
        if(!jsonElement.isJsonObject()) {
            return createEmpty();
        }
        // create DataSet from JsonElement
        return new DataSet(jsonElement.getAsJsonObject());
    }

    public void saveFile(@NonNull DataSet dataSet, String fileName) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if(!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if(!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if(!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }
        }

        // create FileWriter and write it as Json (use Gson)
        try(FileWriter fw = new FileWriter(file)) {
            fw.write(dataSet.data().toString());
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String fileName, String content) {
        // get the file in current folder in /data/%fileName% format if dir or file does not exist create it if that is not possible throw RuntimeException
        File file = new File("data/" + fileName);

        if(!file.exists()) {
            // get parent dir
            File parentDir = file.getParentFile();
            if(!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("Could not create directory " + parentDir.getAbsolutePath());
            }
            try {
                if(!file.createNewFile()) {
                    throw new RuntimeException("Could not create file " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create file " + file.getAbsolutePath(), e);
            }
        }

        // create FileWriter and write it as Json (use Gson)
        try(FileWriter fw = new FileWriter(file)) {
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataSet createEmpty() {
        return new DataSet(new JsonObject());
    }


}
