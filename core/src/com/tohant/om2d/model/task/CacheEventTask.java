package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.storage.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheEventTask implements AsyncTask<Map<String, ?>> {

    private Map<String, ?> cacheSnapshot;

    public CacheEventTask() {
        this.cacheSnapshot = new HashMap<>(Cache.getInstance().getGlobalCache().get());
    }

    @Override
    public Map<String, ?> call() throws Exception {
        return this.cacheSnapshot;
    }

}
