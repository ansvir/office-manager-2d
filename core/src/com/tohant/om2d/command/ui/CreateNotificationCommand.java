package com.tohant.om2d.command.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.UiActorService;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.NOTIFICATION_INFO_LABEL;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.NOTIFICATION_MODAL;

public class CreateNotificationCommand extends AbstractCommand {

    private final GameException e;

    public CreateNotificationCommand(GameException e) {
        this.e = e;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        DefaultModal notification = (DefaultModal) uiActorService.getActorById(NOTIFICATION_MODAL.name());
        notification.getTitleLabel().setText(e.getCode().getType().getTitle());
        notification.getActions().forEach(Action::reset);
        notification.addAction(sequence(alpha(1.0f), delay(4f), fadeOut(3f)));
        notification.setVisible(true);
        GameLabel label = (GameLabel) uiActorService.getActorById(NOTIFICATION_INFO_LABEL.name());
        label.setText(e.getCode().getMessage());
    }

}
