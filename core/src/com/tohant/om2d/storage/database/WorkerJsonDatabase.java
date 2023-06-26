package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.WorkerEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class WorkerJsonDatabase extends JsonDatabase<WorkerEntity> {

    private final Json json;
    private static WorkerJsonDatabase instance;

    private WorkerJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static WorkerJsonDatabase getInstance() {
        if (instance == null) {
            instance = new WorkerJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<WorkerEntity> getById(String id) {
        Iterator<WorkerEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<WorkerEntity> getAll() {
        return json.fromJson(Array.class, WorkerEntity.class, getDbPreferences().getString(WORKERS_TABLE));
    }

    @Override
    public void save(WorkerEntity workerEntity) {
        Array<WorkerEntity> entities = getAll();
        entities.add(workerEntity);
        getDbPreferences().putString(WORKERS_TABLE, json.toJson(entities, Array.class, WorkerEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<WorkerEntity> t) {
        Array<WorkerEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(WORKERS_TABLE, json.toJson(entities, Array.class, WorkerEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<WorkerEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
