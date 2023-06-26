package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.COORD_DELIMITER;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.CURRENT_LEVEL;

public class SelectLevelCommand implements Command {

    private final int index;

    public SelectLevelCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        for (Actor a : uiActorService.getUiActors()) {
            if (a.getName().startsWith(GRID.name())) {
                a.setVisible(false);
            }
        }
        uiActorService.getActorById(GRID.name() + COORD_DELIMITER + index).setVisible(true);
        RuntimeCacheService.getInstance().setLong(CURRENT_LEVEL, index);
    }

}
