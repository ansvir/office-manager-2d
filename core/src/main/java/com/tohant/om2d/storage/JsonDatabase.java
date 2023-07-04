package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public abstract class JsonDatabase<T> implements Database<T> {

    private static final String DATABASE_NAME = "OM2D_DB";
    protected static final String EMPTY_ARRAY = "[]";

    private static final Preferences dbPreferences = Gdx.app.getPreferences(DATABASE_NAME);

    public JsonDatabase() {
        if (checkFirstInit() || isInitButEmpty()) {
            init();
        }
    }

    protected static Preferences getDbPreferences() {
        return dbPreferences;
    }

    private static void init() {
//        dbPreferences.putString(PROGRESSES_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(COMPANIES_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(OFFICES_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(LEVELS_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(CELLS_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(OBJECT_CELLS_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(OBJECT_CELL_ITEM_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(RESIDENTS_TABLE, EMPTY_ARRAY);
//        dbPreferences.putString(WORKERS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(DB_TABLES, EMPTY_ARRAY);
        dbPreferences.flush();
    }

    public static void clearDatabase() {
        init();
    }

    public static boolean checkFirstInit() {
        return dbPreferences == null || dbPreferences.get().isEmpty();
    }

    public static boolean isInitButEmpty() {
        return dbPreferences != null && (dbPreferences.get().isEmpty() || checkDatabaseIsEmpty());
    }

    public static boolean checkDatabaseIsEmpty() {
        return dbPreferences != null && dbPreferences.get().get(DB_TABLES).equals(EMPTY_ARRAY);
    }

    public static void deleteDatabase() {
        dbPreferences.clear();
        dbPreferences.flush();
    }



}
