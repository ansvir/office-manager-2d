package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.Optional;

public abstract class JsonDatabase<T> implements Database<T> {

    private static final String DATABASE_NAME = "OM2D_DB";
    private static final String EMPTY_ARRAY = "[]";

    private final Preferences dbPreferences;

    public JsonDatabase() {
        dbPreferences = Gdx.app.getPreferences(DATABASE_NAME);
        if (dbPreferences.get().isEmpty()) {
            init();
        }
    }

    protected Preferences getDbPreferences() {
        return this.dbPreferences;
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

}
