package com.tohant.om2d.service;

import com.tohant.om2d.storage.CacheProxy;

public class CacheService {

    private final CacheProxy cacheProxy;
    private static CacheService instance;

    private CacheService(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    public static CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService(new CacheProxy());
        }
        return instance;
    }

    public void setValue(String key, String value) {
        this.cacheProxy.setValue(key, value);
    }

    public String getValue(String key) {
        return (String) cacheProxy.getValue(key);
    }

    public void setFloat(String key, float value) {
        this.cacheProxy.setValue(key, value);
    }

    public float getFloat(String key) {
        return Float.parseFloat((String) cacheProxy.getValue(key));
    }

    public void setLong(String key, long value) {
        this.cacheProxy.setValue(key, value);
    }

    public long getLong(String key) {
        return Long.parseLong((String) this.cacheProxy.getValue(key));
    }

    public void setBoolean(String key, boolean value) {
        this.cacheProxy.setValue(key, value);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean((String) this.cacheProxy.getValue(key));
    }

}
