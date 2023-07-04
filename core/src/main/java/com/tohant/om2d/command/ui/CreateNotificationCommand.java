package com.tohant.om2d.command.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.command.Command;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class CreateNotificationCommand implements Command {

    private final GameException e;

    public CreateNotificationCommand(GameException e) {
        this.e = e;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        DefaultModal notification = (DefaultModal) uiActorService.getActorById(UiActorService.UiComponentConstant.NOTIFICATION_MODAL.name());
        notification.getTitleLabel().setText(e.getCode().getType().getTitle());
        notification.getActions().forEach(Action::reset);
        notification.addAction(sequence(alpha(1.0f), delay(4f), fadeOut(3f)));
        notification.setVisible(true);
        GameLabel label = (GameLabel) uiActorService.getActorById(UiActorService.UiComponentConstant.NOTIFICATION_INFO_LABEL.name());
        String message = e.getCode().getMessage();
        float maxWidth = Gdx.graphics.getWidth() / 2f;
        float maxHeight = Gdx.graphics.getHeight() / 4f;
        label.setText(e.getCode().getMessage());
        notification.setWidth(notification.getPrefWidth());
        notification.setHeight(notification.getPrefHeight());
        boolean isFitSize = notification.getWidth() <= maxWidth && notification.getHeight() <= maxHeight;
        while (!isFitSize) {
            label.setText(message.substring(0, message.length() / 2 - 1) + "\n" + message.substring(message.length() / 2 - 1));
            notification.setWidth(notification.getPrefWidth());
            notification.setHeight(notification.getPrefHeight());
            isFitSize = notification.getWidth() <= maxWidth && notification.getHeight() <= maxHeight;
        }
        notification.setPosition(Gdx.graphics.getWidth() / 2f
                - notification.getWidth() / 2f, Gdx.graphics.getHeight() - notification.getHeight() - DEFAULT_PAD);
        notification.setWidth(notification.getPrefWidth());
        AssetService.getInstance().getNotificationSound().play();
    }

}
