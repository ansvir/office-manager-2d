package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class ProgressJsonDatabase extends JsonDatabase<ProgressEntity> {
    
    private final Json json;
    private static ProgressJsonDatabase instance;

    private ProgressJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static ProgressJsonDatabase getInstance() {
        if (instance == null) {
            instance = new ProgressJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<ProgressEntity> getById(String id) {
        Iterator<ProgressEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<ProgressEntity> getAll() {
        return json.fromJson(Array.class, ProgressEntity.class, getDbPreferences().getString(PROGRESSES_TABLE));
    }

    @Override
    public void save(ProgressEntity progressEntity) {
        Array<ProgressEntity> entities = getAll();
        entities.add(progressEntity);
        getDbPreferences().putString(PROGRESSES_TABLE, json.toJson(entities, Array.class, ProgressEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<ProgressEntity> t) {
        Array<ProgressEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(PROGRESSES_TABLE, json.toJson(entities, Array.class, ProgressEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<ProgressEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
