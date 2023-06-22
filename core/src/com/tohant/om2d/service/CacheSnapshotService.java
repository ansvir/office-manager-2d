package com.tohant.om2d.service;

import java.util.Collections;
import java.util.Map;

/**
 * Unmodifiable cache service, because of use of snapshot Map
 */
public class CacheSnapshotService {

    private final Map<String, ?> cacheSnapshot;

    public CacheSnapshotService(Map<String, ?> cacheSnapshot) {
        this.cacheSnapshot = Collections.unmodifiableMap(cacheSnapshot);
    }

    public String getValue(String key) {
        return (String) this.cacheSnapshot.get(key);
    }

    public float getFloat(String key) {
        return Float.parseFloat((String) this.cacheSnapshot.get(key));
    }

    public long getLong(String key) {
        return Long.parseLong((String) this.cacheSnapshot.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean((String) this.cacheSnapshot.get(key));
    }

    public Object getObject(String key) {
        return this.cacheSnapshot.get(key);
    }

}
