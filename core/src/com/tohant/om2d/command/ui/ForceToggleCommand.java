package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.UiActorService;

import java.util.NoSuchElementException;

public class ForceToggleCommand implements Command {

    private final String id;
    private final boolean toggle;

    public ForceToggleCommand(String id, boolean toggle) {
        this.id = id;
        this.toggle = toggle;
    }

    @Override
    public void execute() {
        Actor component = null;
        try {
            component = UiActorService.getInstance().getActorById(id);
        } catch (NoSuchElementException e) {
            component = MenuUiActorService.getInstance().getActorById(id);
        } finally {
            if (component instanceof ToggleActor) {
                ((ToggleActor) component).forceToggle(toggle);
            }
        }
    }
}
