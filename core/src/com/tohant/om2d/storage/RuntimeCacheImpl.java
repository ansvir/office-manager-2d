package com.tohant.om2d.storage;

import java.util.HashMap;
import java.util.Map;

public class RuntimeCacheImpl implements Cache {

    private final Map<String, Object> cache;

    private static RuntimeCacheImpl instance;

    private RuntimeCacheImpl() {
        this.cache = new HashMap<>();
    }

    public static RuntimeCacheImpl getInstance() {
        if (instance == null) {
            instance = new RuntimeCacheImpl();
        }
        return instance;
    }

    @Override
    public void setValue(String key, Object value) {
        this.cache.put(key, value);
    }

    @Override
    public Object getValue(String key) {
        return this.cache.get(key);
    }

}
