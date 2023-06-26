package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.OfficeEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class OfficeJsonDatabase extends JsonDatabase<OfficeEntity> {

    private final Json json;
    private static OfficeJsonDatabase instance;

    private OfficeJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static OfficeJsonDatabase getInstance() {
        if (instance == null) {
            instance = new OfficeJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<OfficeEntity> getById(String id) {
        Iterator<OfficeEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<OfficeEntity> getAll() {
        return json.fromJson(Array.class, OfficeEntity.class, getDbPreferences().getString(OFFICES_TABLE));
    }

    @Override
    public void save(OfficeEntity officeEntity) {
        Array<OfficeEntity> entities = getAll();
        entities.add(officeEntity);
        getDbPreferences().putString(OFFICES_TABLE, json.toJson(entities, Array.class, OfficeEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<OfficeEntity> t) {
        Array<OfficeEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(OFFICES_TABLE, json.toJson(entities, Array.class, OfficeEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<OfficeEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
