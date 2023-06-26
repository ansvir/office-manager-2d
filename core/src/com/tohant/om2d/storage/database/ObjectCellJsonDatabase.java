package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.ObjectCellEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class ObjectCellJsonDatabase extends JsonDatabase<ObjectCellEntity> {

    private final Json json;
    private static ObjectCellJsonDatabase instance;

    private ObjectCellJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static ObjectCellJsonDatabase getInstance() {
        if (instance == null) {
            instance = new ObjectCellJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<ObjectCellEntity> getById(String id) {
        Iterator<ObjectCellEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<ObjectCellEntity> getAll() {
        return json.fromJson(Array.class, ObjectCellEntity.class, getDbPreferences().getString(OBJECT_CELLS_TABLE));
    }

    @Override
    public void save(ObjectCellEntity objectCellEntity) {
        Array<ObjectCellEntity> entities = getAll();
        entities.add(objectCellEntity);
        getDbPreferences().putString(OBJECT_CELLS_TABLE, json.toJson(entities, Array.class, ObjectCellEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<ObjectCellEntity> t) {
        Array<ObjectCellEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(OBJECT_CELLS_TABLE, json.toJson(entities, Array.class, ObjectCellEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<ObjectCellEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
