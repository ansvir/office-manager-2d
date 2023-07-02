package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class LevelJsonDatabase {
//        extends JsonDatabase<LevelEntity> {
//
//    private final Json json;
//    private static LevelJsonDatabase instance;
//
//    private LevelJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static LevelJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new LevelJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<LevelEntity> getById(String id) {
//        Iterator<LevelEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<LevelEntity> getAll() {
//        return json.fromJson(Array.class, LevelEntity.class, getDbPreferences().getString(LEVELS_TABLE));
//    }
//
//    public Array<LevelEntity> getAllByOfficeId(String id) {
//        Array<LevelEntity> result = new Array<>();
//        OfficeJsonDatabase officeJsonDatabase = OfficeJsonDatabase.getInstance();
//        officeJsonDatabase.getById(id)
//                .ifPresent(c -> c.getLevelEntities().iterator().forEach(i -> getById(i).ifPresent(result::add)));
//        return result;
//    }
//
//    @Override
//    public void save(LevelEntity levelEntity) {
//        Array<LevelEntity> entities = getAll();
//        entities.add(levelEntity);
//        CellJsonDatabase cellJsonDatabase = CellJsonDatabase.getInstance();
//        levelEntity.getCellEntities().iterator().forEach(id -> cellJsonDatabase.getById(id).ifPresent(cellJsonDatabase::save));
//        getDbPreferences().putString(LEVELS_TABLE, json.toJson(entities, Array.class, LevelEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<LevelEntity> t) {
//        Array<LevelEntity> entities = getAll();
//        entities.addAll(t);
//        CellJsonDatabase cellJsonDatabase = CellJsonDatabase.getInstance();
//        t.iterator().forEach(l -> l.getCellEntities().iterator().forEach(id -> cellJsonDatabase.getById(id).ifPresent(cellJsonDatabase::save)));
//        getDbPreferences().putString(LEVELS_TABLE, json.toJson(entities, Array.class, LevelEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<LevelEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(LevelEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(LevelEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//            ObjectCellJsonDatabase objectCellJsonDatabase = ObjectCellJsonDatabase.getInstance();
//            objectCellJsonDatabase.deleteAllByIds(e.getCellEntities());
//        });
//    }
//
//    public void deleteAllByIds(Array<String> ids) {
//        ids.iterator().forEach(this::deleteById);
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(LEVELS_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }
//
}
