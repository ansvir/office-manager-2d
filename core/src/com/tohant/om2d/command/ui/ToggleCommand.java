package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.UiActorService;

public class ToggleCommand implements Command {

    private final String id;

    public ToggleCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute() {
        Actor component = UiActorService.getInstance().getActorById(id);
        if (component == null) {
            component = MenuUiActorService.getInstance().getActorById(id);
        }
        if (component instanceof ToggleActor) {
            ((ToggleActor) component).toggle();
        }
    }
}
