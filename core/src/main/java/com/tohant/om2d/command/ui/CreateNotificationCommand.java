package com.tohant.om2d.command.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import lombok.RequiredArgsConstructor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

@Component
@RequiredArgsConstructor
public class CreateNotificationCommand implements Command {

    private final GameCache gameCache;
    private final GameActorSearchService gameActorSearchService;

    @Override
    public void execute() {
        Array<GameException> exceptions = (Array<GameException>) gameCache.getObject(GameCache.GAME_EXCEPTION);
        DefaultModal notification = (DefaultModal) gameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.NOTIFICATION_MODAL.name());
        notification.getTitleLabel().setText("Notification!");
        notification.getActions().forEach(Action::reset);
        notification.addAction(sequence(alpha(1.0f), delay(4f), fadeOut(3f)));
        notification.setVisible(true);
        GameLabel label = (GameLabel) gameActorSearchService.getActorById(GameActorFactory.UiComponentConstant.NOTIFICATION_INFO_LABEL.name());
        for (GameException e : exceptions) {
            String message = e.getCode().getMessage();
            float maxWidth = Gdx.graphics.getWidth() / 2f;
            float maxHeight = Gdx.graphics.getHeight() / 4f;
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
            label.setText(label.getText() + "\n\n" + e.getCode().getMessage());
        }
        AssetService.NOTIFICATION_SOUND.play();
    }

}
