package com.tohant.om2d.storage;

import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.model.Identifiable;
import com.tohant.om2d.model.task.CacheEventTask;

import java.util.Map;

public class CachedEventListener implements AsyncTask<Boolean> {

    private static CachedEventListener instance;
    private volatile boolean isFinished;
    private volatile boolean isConsumed;
    private volatile boolean isPosted;
    private final AsyncExecutor executor;
    private AsyncResult<Map<String, ?>> result;

    private CachedEventListener() {
        executor = new AsyncExecutor(1);
    }

    public static CachedEventListener getInstance() {
        if (instance == null) {
            instance = new CachedEventListener();
        }
        return instance;
    }

    public synchronized void post() {
        result = executor.submit(new CacheEventTask());
        isConsumed = false;
        isPosted = true;
    }

    public synchronized Map<String, ?> consume() {
        isConsumed = true;
        if (result != null && result.isDone()) {
            isPosted = false;
            return result.get();
        } else {
            return null;
        }
    }

    public synchronized void stop() {
        isFinished = true;
    }

    @Override
    public Boolean call() throws Exception {
        while (!isFinished) {
            while (!isPosted && !isFinished) {
            }
            while (!isConsumed && !isFinished) {
            }
        }
        return true;
    }

}
