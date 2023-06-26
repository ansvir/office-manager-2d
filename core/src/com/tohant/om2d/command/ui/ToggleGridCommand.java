package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.CURRENT_LEVEL;

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
        String gridId = GRID.name() + "#" + RuntimeCacheService.getInstance().getLong(CURRENT_LEVEL);
        Actor grid = uiActorService.getActorById(gridId);
        new ToggleCommand(grid.getName()).execute();
    }

}
