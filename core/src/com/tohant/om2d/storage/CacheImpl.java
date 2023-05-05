package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CacheImpl implements Cache {

    private static final String GLOBAL_CACHE_NAME = "GLOBAL";

    public static final String CURRENT_ROOM_TYPE = "CURR_ROOM_TYPE";
    public static final String CURRENT_BUDGET = "CURR_BUDGET";
    public static final String CURRENT_TIME = "CURR_TIME";
    public static final String OFFICES_AMOUNT = "OFFICES_AM";
    public static final String HALLS_AMOUNT = "HALLS_AM";
    public static final String SECURITY_AMOUNT = "SECURITY_AM";
    public static final String CLEANING_AMOUNT = "CLEANING_AM";
    public static final String IS_PAYDAY = "IS_PAYDAY";
    public static final String CURRENT_ROOM = "CURR_ROOM";
    public static final String TOTAL_COSTS = "TOTAL_COSTS";
    public static final String TOTAL_SALARIES = "TOTAL_SALAR";
    public static final String TOTAL_INCOMES = "TOTAL_INC";
    public static final String TOTAL_SECURITY_STAFF = "TOTAL_SEC_STAFF";
    public static final String TOTAL_ADMIN_STAFF = "TOTAL_ADMIN_STAFF";
    public static final String TOTAL_CLEANING_STAFF = "TOTAL_CLEANING_STAFF";
    public static final String TOTAL_WORKERS = "TOTAL_WORKERS";

    private final Preferences globalCache;
    private static CacheImpl cache;

    private CacheImpl() {
        globalCache = Gdx.app.getPreferences(GLOBAL_CACHE_NAME);
    }

    public static CacheImpl getInstance() {
        if (cache == null) {
            cache = new CacheImpl();
        }
        return cache;
    }

    public void setValue(String key, String value) {
        this.globalCache.putString(key, value);
    }

    @Override
    public void setValue(String key, Object value) {
        if (value instanceof String) {
            setValue(key, (String) value);
        } else if (value instanceof Float) {
            setFloat(key, (Float) value);
        } else if (value instanceof Long) {
            setLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            setBoolean(key, (Boolean) value);
        }
    }

    public Object getValue(String key) {
        return this.globalCache.getString(key, null);
    }

    public float getFloatValue(String key) {
        return this.globalCache.getFloat(key);
    }

    public void setFloat(String key, float value) {
        this.globalCache.putFloat(key, value);
    }

    public long getLong(String key) {
        return this.globalCache.getLong(key);
    }

    public void setLong(String key, long value) {
        this.globalCache.putLong(key, value);
    }

    public void setBoolean(String key, boolean value) {
        this.globalCache.putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        return this.globalCache.getBoolean(key);
    }

    public Preferences getGlobalCache() {
        return this.globalCache;
    }

}
