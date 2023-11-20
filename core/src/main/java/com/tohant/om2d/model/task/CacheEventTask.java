package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;

import java.util.HashMap;
import java.util.Map;

public class CacheEventTask implements AsyncTask<Map<String, ?>> {

    private final Map<String, Object> cacheSnapshot;

    public CacheEventTask(Map<String, Object> cache) {
        this.cacheSnapshot = new HashMap<>(cache);
    }

    @Override
    public Map<String, ?> call() throws Exception {
        return this.cacheSnapshot;
    }

}
