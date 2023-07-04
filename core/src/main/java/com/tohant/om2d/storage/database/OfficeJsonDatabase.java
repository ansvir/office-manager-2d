package com.tohant.om2d.storage.database;

public class OfficeJsonDatabase {
//        extends JsonDatabase<OfficeEntity> {
//
//    private final Json json;
//    private static OfficeJsonDatabase instance;
//
//    private OfficeJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static OfficeJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new OfficeJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<OfficeEntity> getById(String id) {
//        Iterator<OfficeEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<OfficeEntity> getAll() {
//        return json.fromJson(Array.class, OfficeEntity.class, getDbPreferences().getString(OFFICES_TABLE));
//    }
//
//    public Array<OfficeEntity> getAllByCompanyId(String id) {
//        Array<OfficeEntity> result = new Array<>();
//        CompanyJsonDatabase companyJsonDatabase = CompanyJsonDatabase.getInstance();
//        companyJsonDatabase.getById(id)
//                .ifPresent(c -> c.getOfficeEntities().iterator().forEach(i -> getById(i).ifPresent(result::add)));
//        return result;
//    }
//
//    @Override
//    public void save(OfficeEntity officeEntity) {
//        Array<OfficeEntity> entities = getAll();
//        entities.add(officeEntity);
//        LevelJsonDatabase levelJsonDatabase = LevelJsonDatabase.getInstance();
//        officeEntity.getLevelEntities().iterator().forEach(id -> levelJsonDatabase.getById(id).ifPresent(levelJsonDatabase::save));
//        ResidentJsonDatabase residentJsonDatabase = ResidentJsonDatabase.getInstance();
//        officeEntity.getResidentEntities().iterator().forEach(id -> residentJsonDatabase.getById(id).ifPresent(residentJsonDatabase::save));
//        getDbPreferences().putString(OFFICES_TABLE, json.toJson(entities, Array.class, ProgressEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<OfficeEntity> t) {
//        Array<OfficeEntity> entities = getAll();
//        entities.addAll(t);
//        LevelJsonDatabase levelJsonDatabase = LevelJsonDatabase.getInstance();
//        t.iterator().forEach(c -> c.getLevelEntities().iterator().forEach(id -> levelJsonDatabase.getById(id).ifPresent(levelJsonDatabase::save)));
//        ResidentJsonDatabase residentJsonDatabase = ResidentJsonDatabase.getInstance();
//        t.iterator().forEach(c -> c.getResidentEntities().iterator().forEach(id -> residentJsonDatabase.getById(id).ifPresent(residentJsonDatabase::save)));
//        getDbPreferences().putString(OFFICES_TABLE, json.toJson(entities, Array.class, ProgressEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<OfficeEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(OfficeEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(OfficeEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//            LevelJsonDatabase levelJsonDatabase = LevelJsonDatabase.getInstance();
//            levelJsonDatabase.deleteAllByIds(e.getLevelEntities());
//            ResidentJsonDatabase residentJsonDatabase = ResidentJsonDatabase.getInstance();
//            residentJsonDatabase.deleteAllByIds(e.getResidentEntities());
//        });
//    }
//
//    public void deleteAllByIds(Array<String> ids) {
//        ids.iterator().forEach(this::deleteById);
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(OFFICES_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }
//
}
