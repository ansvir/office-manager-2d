package com.tohant.om2d.storage;

import com.badlogic.gdx.utils.Array;

import java.util.Optional;

public interface Database<T> {

//    String PROGRESSES_TABLE = "PROGRESSES_TABLE";
//    String COMPANIES_TABLE = "COMPANIES_TABLE";
//    String OFFICES_TABLE = "OFFICES_TABLE";
//    String LEVELS_TABLE = "LEVELS_TABLE";
//    String CELLS_TABLE = "CELLS_TABLE";
//    String OBJECT_CELLS_TABLE = "OBJECT_CELLS_TABLE";
//    String OBJECT_CELL_ITEM_TABLE = "OBJECT_CELL_ITEM_TABLE";
//    String RESIDENTS_TABLE = "RESIDENT_TABLE";
//    String WORKERS_TABLE = "WORKERS_TABLE";

    String DB_TABLES = "DB_TABLES";

    Optional<T> getById(String id);
    Array<T> getAll();
    void save(T t);
    void saveAll(Array<T> t);
    void deleteById(String id);
    void deleteAll();

}
