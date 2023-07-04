package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.storage.RuntimeCacheImpl;

import java.util.HashMap;
import java.util.Map;

public class CacheEventTask implements AsyncTask<Map<String, ?>> {

    private final Map<String, Object> cacheSnapshot;

    public CacheEventTask() {
        this.cacheSnapshot = new HashMap<>(RuntimeCacheImpl.getInstance().getCache());
    }

    @Override
    public Map<String, ?> call() throws Exception {
        return this.cacheSnapshot;
    }

}
