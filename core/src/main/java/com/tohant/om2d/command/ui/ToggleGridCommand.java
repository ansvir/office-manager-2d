package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.LevelDao;

import java.util.UUID;

public class ToggleGridCommand implements Command {

    @Override
    public void execute() {
         toggleGrid();
    }

    private void toggleGrid() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        LevelEntity levelEntity = LevelDao.getInstance().queryForId(UUID.fromString(cache.getValue(Cache.CURRENT_LEVEL_ID)));
        Actor grid = uiActorService.getActorById(levelEntity.getId().toString());
        new ToggleCommand(grid.getName()).execute();
    }

}
