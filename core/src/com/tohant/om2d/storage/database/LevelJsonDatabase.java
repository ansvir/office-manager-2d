package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class LevelJsonDatabase extends JsonDatabase<LevelEntity> {

    private final Json json;
    private static LevelJsonDatabase instance;

    private LevelJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static LevelJsonDatabase getInstance() {
        if (instance == null) {
            instance = new LevelJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<LevelEntity> getById(String id) {
        Iterator<LevelEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<LevelEntity> getAll() {
        return json.fromJson(Array.class, LevelEntity.class, getDbPreferences().getString(LEVELS_TABLE));
    }

    @Override
    public void save(LevelEntity levelEntity) {
        Array<LevelEntity> entities = getAll();
        entities.add(levelEntity);
        getDbPreferences().putString(LEVELS_TABLE, json.toJson(entities, Array.class, LevelEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<LevelEntity> t) {
        Array<LevelEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(LEVELS_TABLE, json.toJson(entities, Array.class, LevelEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<LevelEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
