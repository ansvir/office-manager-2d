package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.ProgressDao;

import java.util.UUID;

public class ToggleGridCommand implements Command {

    @Override
    public void execute() {
         toggleGrid();
    }

    private void toggleGrid() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        ProgressEntity progressEntity = ProgressDao.getInstance().queryForId(UUID.fromString(cache.getValue(Cache.CURRENT_PROGRESS_ID)));
        String officeId = progressEntity.getOfficeEntity().getActorName();
        int level = (int) progressEntity.getLevelEntity().getLevel();
        String gridId = ServiceUtil.getGridActorId(level, officeId);
        Actor grid = uiActorService.getActorById(gridId);
        new ToggleCommand(grid.getName()).execute();
    }

}
