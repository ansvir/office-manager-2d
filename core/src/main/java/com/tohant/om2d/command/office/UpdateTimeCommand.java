package com.tohant.om2d.command.office;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.UiActorService.UiComponentConstant.TIMELINE_LABEL;

public class UpdateTimeCommand implements Command {

    private final String time;

    public UpdateTimeCommand(String time) {
        this.time = time;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        Label label = (Label) uiActorService.getActorById(TIMELINE_LABEL.name());
        label.setText(time);
        if (label.getX() + label.getWidth() >= Gdx.graphics.getWidth()) {
            label.setSize(label.getWidth() + 20, label.getHeight());
        }
    }
}
