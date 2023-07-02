package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class CompanyJsonDatabase {
//        extends JsonDatabase<CompanyEntity> {
//
//    private final Json json;
//    private static CompanyJsonDatabase instance;
//
//    private CompanyJsonDatabase() {
//        json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        json.setEnumNames(true);
//    }
//
//    public static CompanyJsonDatabase getInstance() {
//        if (instance == null) {
//            instance = new CompanyJsonDatabase();
//        }
//        return instance;
//    }
//
//    @Override
//    public Optional<CompanyEntity> getById(String id) {
//        Iterator<CompanyEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
//        if (itr.hasNext()) {
//            return Optional.of(itr.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    public Optional<CompanyEntity> findCompanyByProgressId(String id) {
//        ProgressJsonDatabase progressJsonDatabase = ProgressJsonDatabase.getInstance();
//        Iterator<CompanyEntity> company = getAll().select(c -> progressJsonDatabase.getAll()
//                .select(p -> p.getCompanyEntity().getId().equals(c.getId())).iterator().hasNext()).iterator();
//        if (company.hasNext()) {
//            return Optional.of(company.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    public Array<CompanyEntity> getAll() {
//        Array<CompanyEntity> result = new Array<>();
//        json.fromJson(Array.class, ProgressEntity.class, getDbPreferences().getString(DB_TABLES))
//                .iterator().forEach(p -> result.add(((ProgressEntity) p).getCompanyEntity()));
//        return result;
//    }
//
//    @Override
//    public void save(CompanyEntity companyEntity) {
//        getDbPreferences().putString(DB_TABLES, json.toJson(entities, Array.class, ProgressEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void saveAll(Array<CompanyEntity> t) {
//        Array<CompanyEntity> entities = getAll();
//        entities.addAll(t);
//        OfficeJsonDatabase officeJsonDatabase = OfficeJsonDatabase.getInstance();
//        t.iterator().forEach(c -> c.getOfficeEntities().iterator().forEach(id -> officeJsonDatabase.getById(id).ifPresent(officeJsonDatabase::save)));
//        getDbPreferences().putString(DB_TABLES, json.toJson(entities, Array.class, ProgressEntity.class));
//        getDbPreferences().flush();
//    }
//
//    @Override
//    public void deleteById(String id) {
//        getById(id).ifPresent(e -> {
//            Array<CompanyEntity> filtered = new Array<>(Arrays.stream(getAll().toArray(CompanyEntity.class))
//                    .filter(e1 -> !e1.getId().equals(id)).toArray(CompanyEntity[]::new));
//            deleteAll();
//            saveAll(filtered);
//            OfficeJsonDatabase officeJsonDatabase = OfficeJsonDatabase.getInstance();
//            officeJsonDatabase.deleteAllByIds(e.getOfficeEntities());
//        });
//    }
//
//    @Override
//    public void deleteAll() {
//        getDbPreferences().putString(COMPANIES_TABLE, EMPTY_ARRAY);
//        getDbPreferences().flush();
//    }
//
}
