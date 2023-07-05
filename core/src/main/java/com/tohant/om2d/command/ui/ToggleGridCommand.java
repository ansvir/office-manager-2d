package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.storage.database.ProgressJsonDatabase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ToggleGridCommand implements Command {

    private final Queue<String> actorsToToggleQueue;

    public ToggleGridCommand() {
        this.actorsToToggleQueue = new ConcurrentLinkedQueue<>();
        this.actorsToToggleQueue.add(UiActorService.UiComponentConstant.GRID.name());
    }

    @Override
    public void execute() {
        String actorId = actorsToToggleQueue.poll();
         if (actorId.startsWith(UiActorService.UiComponentConstant.GRID.name())) {
            toggleGrid();
            actorsToToggleQueue.add(UiActorService.UiComponentConstant.GRID.name());
        }
    }

    private void toggleGrid() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        ProgressEntity progressEntity = ProgressJsonDatabase.getInstance().getById(cache.getValue(Cache.CURRENT_PROGRESS_ID)).get();
        String officeId = progressEntity.getOfficeEntity().getActorName();
        int level = (int) progressEntity.getLevelEntity().getLevel();
        String gridId = ServiceUtil.getGridActorId(level, officeId);
        Actor grid = uiActorService.getActorById(gridId);
        new ToggleCommand(grid.getName()).execute();
    }

}
