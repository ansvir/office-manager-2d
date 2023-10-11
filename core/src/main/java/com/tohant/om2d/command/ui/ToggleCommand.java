package com.tohant.om2d.command.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.MenuUiActorService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.COORD_DELIMITER;

public class ToggleCommand implements Command {

    private final String id;

    public ToggleCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute() {
        Actor component = null;
        String componentId = id.split(COORD_DELIMITER)[0];
        try {
            UiActorService.UiComponentConstant.valueOf(componentId);
            component = UiActorService.getInstance().getActorById(id);
        } catch (IllegalArgumentException e) {
            try {
                MenuUiActorService.MenuUiComponentConstant.valueOf(componentId);
                component = MenuUiActorService.getInstance().getActorById(id);
            } catch (IllegalArgumentException ignored) {

            }
        } finally {
            if (component instanceof ToggleActor) {
                ((ToggleActor) component).toggle();
            }
        }
    }
}
