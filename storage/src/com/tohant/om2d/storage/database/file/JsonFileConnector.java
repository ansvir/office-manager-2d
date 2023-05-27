package com.tohant.om2d.storage.database.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.tohant.om2d.storage.database.file.model.FileTable;
import com.tohant.om2d.storage.database.file.model.JsonTable;
import com.tohant.om2d.storage.entity.RoomEntity;

import static com.tohant.om2d.storage.constant.TableConstant.DB_LOCAL_PATH;
import static com.tohant.om2d.storage.constant.TableConstant.ROOM_TABLE;

public class JsonFileConnector implements FileConnector {

    private static final String JSON_DB = DB_LOCAL_PATH + "json/";
    private static final String EXT = ".json";

    private static JsonFileConnector instance;
    private FileTable[] tables;

    private JsonFileConnector() {}

    public static JsonFileConnector getInstance() {
        if (instance == null) {
            instance = new JsonFileConnector();
        }
        return instance;
    }

    @Override
    public FileTable connect(String dbPath) {
        FileHandle[] db = Gdx.files.local(JSON_DB).list();
        FileTable[] tables = new JsonTable[db.length];
//        for (int i = 0; i < db.length; i++) {
//            Model nextModel = null;
//            switch (db[i].nameWithoutExtension().toUpperCase()) {
//                case ROOM_TABLE: nextModel = new RoomEntity(); break;
//            }
//            if (nextModel != null) {
//                tables[i] = new JsonTable(db[i].name(), readJson(db[i]));
//            }
//
//        }
        this.tables = tables;
        return this.tables[0];
    }

//    private Model readJson(FileHandle file, Model model) {
//        Json json = new Json();
//        return json.fromJson(model.getClass(), file);
//    }

    public enum Table {
        ROOM(JSON_DB + "room.json"), STAFF(JSON_DB + "staff.json"),
        GAME_PROGRESS(JSON_DB + "game_progress.json");

        private final String path;

        Table(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

    }

}
