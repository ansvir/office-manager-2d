package com.tohant.om2d.storage.database;

public class ProgressJsonFileDatabase {
//        extends JsonFileDatabase<ProgressEntity> {
    
//    private final Json json;
//    private static ProgressJsonFileDatabase instance;
//
//    private ProgressJsonFileDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static ProgressJsonFileDatabase getInstance() {
//        if (instance == null) {
//            instance = new ProgressJsonFileDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<ProgressEntity> getById(String id) {
//        Iterator<ProgressEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<ProgressEntity> getAll() {
//        return json.fromJson(Array.class, ProgressEntity.class, getJsonFileDatabasePreferences().getString(DB_TABLES));
//    }
//
//    @Override
//    public void save(ProgressEntity progressEntity) {
//        Array<ProgressEntity> entities = getAll();
//        entities.add(progressEntity);
//        getJsonFileDatabasePreferences().putString(DB_TABLES, json.toJson(entities, Array.class, ProgressEntity.class));
//        getJsonFileDatabasePreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<ProgressEntity> t) {
//        Array<ProgressEntity> entities = getAll();
//        entities.addAll(t);
//        getJsonFileDatabasePreferences().putString(DB_TABLES, json.toJson(entities, Array.class, ProgressEntity.class));
//        getJsonFileDatabasePreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<ProgressEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(ProgressEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(ProgressEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//        });
//    }
//
//    @Override
//    public void deleteAll() {
//        getJsonFileDatabasePreferences().putString(DB_TABLES, EMPTY_ARRAY);
//        getJsonFileDatabasePreferences().flush();
//    }

}
