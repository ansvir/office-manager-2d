package com.tohant.om2d.storage;

import java.util.function.Consumer;

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
        this.initCallback = (c) -> {};
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

}
