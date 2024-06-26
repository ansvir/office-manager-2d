package com.tohant.om2d.storage.cache;

import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.model.task.CacheEventTask;
import com.tohant.om2d.service.CacheSnapshotService;

import java.util.Map;
import java.util.function.Consumer;

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
        if (result == null) {
            result = executor.submit(new CacheEventTask());
        } else {
            if (!isConsumed) {
                consume();
                if (result.isDone()) {
                    result.get();
                    result = executor.submit(new CacheEventTask());
                }
            }
        }
        isConsumed = false;
        isPosted = true;
    }

    public synchronized Map<String, ?> consume() {
        isConsumed = true;
        if (result != null && result.isDone() && isPosted) {
            isPosted = false;
            return result.get();
        } else {
            return null;
        }
    }

    public synchronized void onEventConditional(Consumer<CacheSnapshotService> onConsume, Runnable onPost, boolean condition) {
        if (condition) {
            Map<String, ?> cacheSnapshot = consume();
            CacheSnapshotService snapshotService = new CacheSnapshotService(cacheSnapshot);
            onConsume.accept(snapshotService);
        } else {
            onPost.run();
            post();
        }
    }

    public synchronized void onEvent(Consumer<CacheSnapshotService> onConsume) {
        Map<String, ?> cacheSnapshot = consume();
        CacheSnapshotService snapshotService = new CacheSnapshotService(cacheSnapshot);
        onConsume.accept(snapshotService);
        post();
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

    public boolean isConsumed() {
        return isConsumed;
    }

}
