package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.CURRENT_LEVEL;

public class SelectLevelCommand extends AbstractCommand {

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
        uiActorService.getActorById(GRID.name() + "#" + index).setVisible(true);
        getRuntimeCacheService().setLong(CURRENT_LEVEL, index);
    }

}
