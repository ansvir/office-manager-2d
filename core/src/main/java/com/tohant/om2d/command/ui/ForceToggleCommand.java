package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.Command;
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
            UiActorService.UiComponentConstant.valueOf(id);
            component = UiActorService.getInstance().getActorById(id);
        } catch (IllegalArgumentException e) {
            try {
                MenuUiActorService.MenuUiComponentConstant.valueOf(id);
                component = MenuUiActorService.getInstance().getActorById(id);
            } catch (IllegalArgumentException ignored) {

            }
        } finally {
            if (component instanceof ToggleActor) {
                ((ToggleActor) component).forceToggle(toggle);
            }
        }
    }
}
