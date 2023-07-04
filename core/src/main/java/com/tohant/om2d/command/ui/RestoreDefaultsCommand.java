package com.tohant.om2d.command.ui;

import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.CURRENT_ITEM;
import static com.tohant.om2d.storage.Cache.CURRENT_ROOM_TYPE;

public class RestoreDefaultsCommand implements Command {

    @Override
    public void execute() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        runtimeCache.setObject(CURRENT_ITEM, null);
        runtimeCache.setValue(CURRENT_ROOM_TYPE, null);
        AssetService.getInstance().setCursor(AssetService.GameCursor.MAIN);
    }

}
