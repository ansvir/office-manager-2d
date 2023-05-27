package com.tohant.om2d.command;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.CacheService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

public abstract class AbstractCommand implements Command {

    private final CacheService cacheService;
    private final RuntimeCacheService runtimeCacheService;

    public AbstractCommand() {
        this.cacheService = CacheService.getInstance();
        this.runtimeCacheService = RuntimeCacheService.getInstance();
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public RuntimeCacheService getRuntimeCacheService() {
        return runtimeCacheService;
    }

}
