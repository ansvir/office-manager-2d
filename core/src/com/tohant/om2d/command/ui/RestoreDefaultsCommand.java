package com.tohant.om2d.command.ui;

import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.*;
import static com.tohant.om2d.storage.Cache.CURRENT_OBJECT_CELL;

public class RestoreDefaultsCommand extends AbstractCommand {

    @Override
    public void execute() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        runtimeCache.setValue(CURRENT_ROOM, null);
        runtimeCache.setObject(CURRENT_ITEM, null);
        runtimeCache.setValue(CURRENT_ROOM_TYPE, null);
        runtimeCache.setObject(CURRENT_OBJECT_CELL, null);
        AssetService.getInstance().setCursor(AssetService.GameCursor.MAIN);
    }

}
