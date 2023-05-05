package com.tohant.om2d.storage;

import java.util.function.Consumer;

import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.TOTAL_COSTS;

public class CacheProxy implements Cache {

    private CacheImpl cache;
    private Consumer<Cache> initCallback;
    private Consumer<Cache> setCallback;
    private Consumer<Cache> getCallback;

    public CacheProxy(Consumer<Cache> setCallback, Consumer<Cache> getCallback, Consumer<Cache> initCallback) {
        cache = CacheImpl.getInstance();
        this.setCallback = setCallback;
        this.getCallback = getCallback;
        this.initCallback = initCallback;
        this.initCallback.accept(cache);
    }

    public CacheProxy() {
        cache = CacheImpl.getInstance();
        this.setCallback = (c) -> {};
        this.getCallback = (c) -> {};
        this.initCallback = getDefaultInit();
        this.initCallback.accept(cache);
    }

    @Override
    public void setValue(String key, Object value) {
        setCallback.accept(cache);
        cache.setValue(key, value);
    }

    @Override
    public Object getValue(String key) {
        getCallback.accept(cache);
        return cache.getGlobalCache().get().get(key);
    }

    private Consumer<Cache> getDefaultInit() {
        return (c) -> {
            c.setValue(CURRENT_ROOM_TYPE, null);
            c.setValue(CURRENT_BUDGET, 2000.0f);
            c.setValue(CURRENT_TIME, "01/01/0001");
            c.setValue(OFFICES_AMOUNT, 0L);
            c.setValue(HALLS_AMOUNT, 0L);
            c.setValue(SECURITY_AMOUNT, 0L);
            c.setValue(CLEANING_AMOUNT, 0L);
            c.setValue(IS_PAYDAY, false);
            c.setValue(CURRENT_ROOM, null);
            c.setValue(TOTAL_COSTS, 0.0f);
            c.setValue(TOTAL_INCOMES, 0.0f);
        };
    }

}
