package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CacheImpl implements Cache {

    private static final String GLOBAL_CACHE_NAME = "GLOBAL";

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
