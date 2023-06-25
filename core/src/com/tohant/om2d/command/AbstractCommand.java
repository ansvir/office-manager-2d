package com.tohant.om2d.command;

import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;

public abstract class AbstractCommand implements Command {

    private final RuntimeCacheService runtimeCacheService;

    public AbstractCommand() {
        this.runtimeCacheService = RuntimeCacheService.getInstance();
    }

    public RuntimeCacheService getRuntimeCacheService() {
        return runtimeCacheService;
    }

}
