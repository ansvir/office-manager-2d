package com.tohant.om2d.storage.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.storage.Database;

import java.util.HashMap;
import java.util.Map;

public abstract class JsonFileDatabase<T> implements Database<T> {

    private static final String JSON_FILE_DB_PATH = "data/app";
    private static final String JSON_FILE_DB_KEY = "JSON_FILE_DB";
    protected static final String JSON_FILE_DB_EVENTS_KEY = "events_";
    protected static final String EMPTY_ARRAY = "[]";

    private static final String JSON_FILE_DB_EVENTS_PATH = JSON_FILE_DB_PATH + "/events";

    private final Preferences dbPreferences = Gdx.app.getPreferences(JSON_FILE_DB_KEY);

    private final Json json;

    public JsonFileDatabase() {
        if (checkFirstInit() || isInitButEmpty()) {
            init();
        }
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setEnumNames(true);
    }

    protected Preferences getJsonFileDatabasePreferences() {
        return dbPreferences;
    }

    private void init() {
        Map<String, String> jsonsMap = new HashMap<>();
        FileHandle[] eventsFiles = Gdx.files.local(JSON_FILE_DB_EVENTS_PATH).list();
        for (FileHandle f : eventsFiles) {
            String name = validateFileName(f.file().getName(), JSON_FILE_DB_EVENTS_KEY);
            String content = f.readString();
            jsonsMap.put(name, content);
        }
        dbPreferences.put(jsonsMap);
        dbPreferences.flush();
    }

    private String validateFileName(String fileName, String prefix) {
        if (fileName.startsWith(prefix) && fileName.endsWith(".json")
                && (fileName.contains("global") || fileName.contains("local"))) {
            return fileName;
        } else {
            clearDatabase();
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
    }

    public void clearDatabase() {
        init();
    }

    public boolean checkFirstInit() {
        return dbPreferences == null;
    }

    public boolean isInitButEmpty() {
        return dbPreferences != null && checkDatabaseIsEmpty();
    }

    public boolean checkDatabaseIsEmpty() {
        return dbPreferences != null && dbPreferences.get().isEmpty();
    }

    protected Array<String> getJsonFileDbFile(String key) {
        return new Array<>(dbPreferences.get().entrySet().stream()
                .filter(e -> e.getKey().startsWith(key))
                .map(e -> (String) e.getValue()).toArray(String[]::new));
    }

    public void deleteDatabase() {
        dbPreferences.clear();
        dbPreferences.flush();
    }

    protected Json getJson() {
        return this.json;
    }

}
