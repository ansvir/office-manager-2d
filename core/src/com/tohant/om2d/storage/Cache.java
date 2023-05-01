package com.tohant.om2d.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Cache {

    private static final String GLOBAL_CACHE_NAME = "GLOBAL";

    private final Preferences globalCache;

    public Cache() {
        globalCache = Gdx.app.getPreferences(GLOBAL_CACHE_NAME);
    }

    public void setValue(String key, String value) {
        this.globalCache.putString(key, value);
    }

    public String getValue(String key) {
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

    public Preferences getGlobalCache() {
        return this.globalCache;
    }

}
