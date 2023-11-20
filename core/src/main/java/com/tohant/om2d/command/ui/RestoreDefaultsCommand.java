package com.tohant.om2d.command.ui;

import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.tohant.om2d.storage.cache.GameCache.CURRENT_ITEM;
import static com.tohant.om2d.storage.cache.GameCache.CURRENT_ROOM_TYPE;

@Component
@RequiredArgsConstructor
public class RestoreDefaultsCommand implements Command {

    private final GameCache gameCache;

    @Override
    public void execute() {
        gameCache.setObject(CURRENT_ITEM, null);
        gameCache.setValue(CURRENT_ROOM_TYPE, null);
        AssetService.setCursor(AssetService.GameCursor.MAIN);
    }

}
