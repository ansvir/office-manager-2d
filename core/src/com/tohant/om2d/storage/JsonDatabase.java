package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class JsonDatabase<T> implements Database<T> {

    private static final String DATABASE_NAME = "OM2D_DB";
    private static final String EMPTY_ARRAY = "[]";

    private static final Preferences dbPreferences = Gdx.app.getPreferences(DATABASE_NAME);

    public JsonDatabase() {
        if (checkFirstInit() || isInitButEmpty()) {
            init();
        }
    }

    protected static Preferences getDbPreferences() {
        return dbPreferences;
    }

    public static void init() {
        dbPreferences.putString(PROGRESSES_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(COMPANIES_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OFFICES_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(LEVELS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(CELLS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OBJECT_CELLS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OBJECT_CELL_ITEM_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(RESIDENTS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(WORKERS_TABLE, EMPTY_ARRAY);
        dbPreferences.flush();
    }

    public static boolean checkDatabaseIsEmpty() {
        AtomicInteger amountOfEmpty = new AtomicInteger(0);
        Map<String, ?> entries = dbPreferences.get();
        entries.values().forEach(v -> {
            if (v.equals(EMPTY_ARRAY)) {
                amountOfEmpty.incrementAndGet();
            }
        });
        return entries.size() == 9 && amountOfEmpty.get() == 9;
    }

    public static boolean checkFirstInit() {
        return dbPreferences == null || dbPreferences.get().isEmpty();
    }

    public static boolean isInitButEmpty() {
        return dbPreferences != null && dbPreferences.get().isEmpty();
    }

    public static void clearDatabase() {
        dbPreferences.clear();
        dbPreferences.flush();
    }

}
