package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.ObjectCellItemEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class ObjectCellItemJsonDatabase {
//        extends JsonDatabase<ObjectCellItemEntity> {
//
//    private final Json json;
//    private static ObjectCellItemJsonDatabase instance;
//
//    private ObjectCellItemJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static ObjectCellItemJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new ObjectCellItemJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<ObjectCellItemEntity> getById(String id) {
//        Iterator<ObjectCellItemEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<ObjectCellItemEntity> getAll() {
//        return json.fromJson(Array.class, ObjectCellItemEntity.class, getDbPreferences().getString(OBJECT_CELL_ITEM_TABLE));
//    }
//
//    @Override
//    public void save(ObjectCellItemEntity objectCellItemEntity) {
//        Array<ObjectCellItemEntity> entities = getAll();
//        entities.add(objectCellItemEntity);
//        ObjectCellItemJsonDatabase objectCellItemJsonDatabase = ObjectCellItemJsonDatabase.getInstance();
//        ob
//        getDbPreferences().putString(OBJECT_CELL_ITEM_TABLE, json.toJson(entities, Array.class, ObjectCellItemEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<ObjectCellItemEntity> t) {
//        Array<ObjectCellItemEntity> entities = getAll();
//        entities.addAll(t);
//        getDbPreferences().putString(OBJECT_CELL_ITEM_TABLE, json.toJson(entities, Array.class, ObjectCellItemEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<ObjectCellItemEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(ObjectCellItemEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(ObjectCellItemEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//        });
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(OBJECT_CELL_ITEM_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }
//
}
