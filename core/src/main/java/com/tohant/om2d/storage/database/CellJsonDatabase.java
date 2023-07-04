package com.tohant.om2d.storage.database;

public class CellJsonDatabase {
//        extends JsonDatabase<CellEntity> {
//
//    private final Json json;
//    private static CellJsonDatabase instance;
//
//    private CellJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static CellJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new CellJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<CellEntity> getById(String id) {
//        Iterator<CellEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<CellEntity> getAll() {
//
//        return json.fromJson(Array.class, ProgressEntity.class, getDbPreferences().getString(DB_TABLES)).iterator().forEach();
//    }
//
//    public Array<CellEntity> getAllByLevelId(String id) {
//        Array<CellEntity> result = new Array<>();
//        LevelJsonDatabase levelJsonDatabase = LevelJsonDatabase.getInstance();
//        levelJsonDatabase.getById(id).ifPresent(c -> {
//            Iterator<String> ids = c.getCellEntities().select(i -> getById(i).isPresent()).iterator();
//            Array<CellEntity> cells = getAll();
//            while (ids.hasNext()) {
//                cells.select(o -> o.getId().equals(ids.next())).forEach(result::add);
//            }
//        });
//        return result;
//    }
//
//    @Override
//    public void save(CellEntity cellEntity) {
//        Array<CellEntity> entities = getAll();
//        entities.add(cellEntity);
//        ObjectCellJsonDatabase objectCellJsonDatabase = ObjectCellJsonDatabase.getInstance();
//        cellEntity.getObjectCellEntities().iterator().forEach(id -> objectCellJsonDatabase.getById(id).ifPresent(objectCellJsonDatabase::save));
//        getDbPreferences().putString(CELLS_TABLE, json.toJson(entities, Array.class, CellEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<CellEntity> t) {
//        Array<CellEntity> entities = getAll();
//        entities.addAll(t);
//        ObjectCellJsonDatabase objectCellJsonDatabase = ObjectCellJsonDatabase.getInstance();
//        t.iterator().forEach(c -> c.getObjectCellEntities().iterator().forEach(id -> objectCellJsonDatabase.getById(id).ifPresent(objectCellJsonDatabase::save)));
//        getDbPreferences().putString(CELLS_TABLE, json.toJson(entities, Array.class, CellEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<CellEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(CellEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(CellEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//            ObjectCellJsonDatabase objectCellJsonDatabase = ObjectCellJsonDatabase.getInstance();
//            objectCellJsonDatabase.deleteAllByIds(e.getCellsIds());
//        });
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(CELLS_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }

}
