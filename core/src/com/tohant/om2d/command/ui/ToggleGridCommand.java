package com.tohant.om2d.command.ui;

import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.command.ui.ToggleCommand;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.CURRENT_LEVEL;

public class ToggleGridCommand extends AbstractCommand {
    @Override
    public void execute() {
        String levelId = GRID.name() + "#" + getRuntimeCacheService().getLong(CURRENT_LEVEL);
        new ToggleCommand(levelId).execute();
    }

}
