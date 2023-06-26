package com.tohant.om2d.storage.database;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.storage.JsonDatabase;

import java.util.Iterator;
import java.util.Optional;

public class CompanyJsonDatabase extends JsonDatabase<CompanyEntity> {

    private final Json json;
    private static CompanyJsonDatabase instance;

    private CompanyJsonDatabase() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static CompanyJsonDatabase getInstance() {
        if (instance == null) {
            instance = new CompanyJsonDatabase();
        }
        return instance;
    }

    @Override
    public Optional<CompanyEntity> getById(String id) {
        Iterator<CompanyEntity> itr = getAll().select(e -> e.getId().equals(id)).iterator();
        if (itr.hasNext()) {
            return Optional.of(itr.next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Array<CompanyEntity> getAll() {
        return json.fromJson(Array.class, CompanyEntity.class, getDbPreferences().getString(COMPANIES_TABLE));
    }

    @Override
    public void save(CompanyEntity companyEntity) {
        Array<CompanyEntity> entities = getAll();
        entities.add(companyEntity);
        getDbPreferences().putString(COMPANIES_TABLE, json.toJson(entities, Array.class, CompanyEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void saveAll(Array<CompanyEntity> t) {
        Array<CompanyEntity> entities = getAll();
        entities.addAll(t);
        getDbPreferences().putString(COMPANIES_TABLE, json.toJson(entities, Array.class, CompanyEntity.class));
        getDbPreferences().flush();
    }

    @Override
    public void deleteById(String id) {
        getById(id).ifPresent(e -> {
            Array<CompanyEntity> all = getAll();
            all.removeValue(e, false);
            saveAll(all);
        });
    }
}
