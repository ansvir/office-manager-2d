package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.database.ProgressJsonDatabase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.tohant.om2d.service.ServiceUtil.getGridActorId;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.CURRENT_PROGRESS_ID;

public class ToggleGridCommand implements Command {

    private final Queue<String> actorsToToggleQueue;

    public ToggleGridCommand() {
        this.actorsToToggleQueue = new ConcurrentLinkedQueue<>();
        this.actorsToToggleQueue.add(GRID.name());
    }

    @Override
    public void execute() {
        String actorId = actorsToToggleQueue.poll();
         if (actorId.startsWith(GRID.name())) {
            toggleGrid();
            actorsToToggleQueue.add(GRID.name());
        }
    }

    private void toggleGrid() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        ProgressEntity progressEntity = ProgressJsonDatabase.getInstance().getById(cache.getValue(CURRENT_PROGRESS_ID)).get();
        String companyId = progressEntity.getCompanyEntity().getId();
        String officeId = progressEntity.getOfficeEntity().getId();
        int level = (int) progressEntity.getLevelEntity().getLevel();
        String gridId = getGridActorId(level, officeId, companyId);
        Actor grid = uiActorService.getActorById(gridId);
        new ToggleCommand(grid.getName()).execute();
    }

}
