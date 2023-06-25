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
        if (checkFirstInit()) {
            init();
        }
    }

    protected static Preferences getDbPreferences() {
        return dbPreferences;
    }

    private void init() {
        dbPreferences.putString(COMPANIES_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OFFICES_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(LEVELS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(CELLS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OBJECT_CELLS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(OBJECT_CELL_ITEM_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(RESIDENTS_TABLE, EMPTY_ARRAY);
        dbPreferences.putString(WORKERS_TABLE, EMPTY_ARRAY);
    }

    public static boolean checkDatabaseIsEmpty() {
        AtomicInteger amountOfEmpty = new AtomicInteger(0);
        Map<String, ?> entries = dbPreferences.get();
        entries.values().forEach(v -> {
            if (v.equals(EMPTY_ARRAY)) {
                amountOfEmpty.incrementAndGet();
            }
        });
        return entries.size() == 8 && amountOfEmpty.get() == 8;
    }

    private static boolean checkFirstInit() {
        return dbPreferences == null || dbPreferences.get().isEmpty();
    }

    public static void clearDatabase() {
        dbPreferences.get().clear();
    }

}
