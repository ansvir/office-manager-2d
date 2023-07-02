package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.ResidentEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class ResidentJsonDatabase {
//        extends JsonDatabase<ResidentEntity> {
//
//    private final Json json;
//    private static ResidentJsonDatabase instance;
//
//    private ResidentJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static ResidentJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new ResidentJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<ResidentEntity> getById(String id) {
//        Iterator<ResidentEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<ResidentEntity> getAll() {
//        return json.fromJson(Array.class, ResidentEntity.class, getDbPreferences().getString(RESIDENTS_TABLE));
//    }
//
//    @Override
//    public void save(ResidentEntity residentEntity) {
//        Array<ResidentEntity> entities = getAll();
//        entities.add(residentEntity);
//        WorkerJsonDatabase workerJsonDatabase = WorkerJsonDatabase.getInstance();
//        residentEntity.getWorkerEntities().iterator().forEach(id -> workerJsonDatabase.getById(id).ifPresent(workerJsonDatabase::save));
//        getDbPreferences().putString(RESIDENTS_TABLE, json.toJson(entities, Array.class, ResidentEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<ResidentEntity> t) {
//        Array<ResidentEntity> entities = getAll();
//        entities.addAll(t);
//        WorkerJsonDatabase workerJsonDatabase = WorkerJsonDatabase.getInstance();
//        t.iterator().forEach(e -> e.getWorkerEntities().iterator().forEach(id -> workerJsonDatabase.getById(id).ifPresent(workerJsonDatabase::save)));
//        getDbPreferences().putString(RESIDENTS_TABLE, json.toJson(entities, Array.class, ResidentEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<ResidentEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(ResidentEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(ResidentEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//            WorkerJsonDatabase workerJsonDatabase = WorkerJsonDatabase.getInstance();
//            workerJsonDatabase.deleteAllByIds(e.getWorkerEntities());
//        });
//    }
//
//    public void deleteAllByIds(Array<String> ids) {
//        ids.iterator().forEach(this::deleteById);
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(RESIDENTS_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }
//
}
