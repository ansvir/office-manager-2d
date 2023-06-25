package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class CellJsonDatabase extends JsonDatabase<CellEntity> {

    private final Json json;
    private static CellJsonDatabase instance;

    private CellJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static CellJsonDatabase getInstance() {
        if (instance == null) {
            instance = new CellJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<CellEntity> getById(String id) {
        Iterator<CellEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<CellEntity> getAll() {
        return json.fromJson(Array.class, CellEntity.class, getDbPreferences().getString(CELLS_TABLE));
    }

    @Override
    public void save(CellEntity cellEntity) {
        Array<CellEntity> entities = getAll();
        entities.add(cellEntity);
        getDbPreferences().putString(CELLS_TABLE, json.toJson(entities, Array.class, CellEntity.class));
    }

    @Override
    public void saveAll(Array<CellEntity> t) {
        Array<CellEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(CELLS_TABLE, json.toJson(entities, Array.class, CellEntity.class));
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<CellEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
