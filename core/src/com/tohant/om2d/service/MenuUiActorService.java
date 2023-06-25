package com.tohant.om2d.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.util.AssetsUtil;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.MenuUiActorService.MenuUiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.MENU_UI_ACTORS;

public class MenuUiActorService extends ActorService {

    private final Skin skin;

    private static MenuUiActorService instance;

    private MenuUiActorService() {
        this.skin = AssetsUtil.getDefaultSkin();
        initMenuScreen();
    }

    public static MenuUiActorService getInstance() {
        if (instance == null) {
            instance = new MenuUiActorService();
        }
        return instance;
    }

    private void initMenuScreen() {
        RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
        Array<Actor> menuUiActors = (Array<Actor>) runtimeCache.getObject(MENU_UI_ACTORS);
        menuUiActors.add(createNotificationModal());
    }

    private DefaultModal createNotificationModal() {
        DefaultModal notification = new DefaultModal(MENU_NOTIFICATION_MODAL.name(), "",
                Array.with(new GameLabel(MENU_NOTIFICATION_INFO_LABEL.name(), "", skin)), createCloseNotificationButton(), skin);
        notification.setMovable(false);
        notification.setResizable(false);
        notification.addAction(sequence(delay(4f), fadeOut(3f)));
        notification.setWidth(Gdx.graphics.getWidth() / 4f);
        notification.setHeight(notification.getPrefHeight());
        notification.setPosition(Gdx.graphics.getWidth() / 2f
                - notification.getWidth() / 2f, Gdx.graphics.getHeight() - notification.getHeight() - DEFAULT_PAD);
        notification.setVisible(false);
        return notification;
    }

    private AbstractTextButton createCloseNotificationButton() {
        return new GameTextButton(MENU_CLOSE_NOTIFICATION_BUTTON.name(), new ToggleCommand(MENU_NOTIFICATION_MODAL.name()), "X", skin);
    }

    @Override
    public Array<Actor> getUiActors() {
        return (Array<Actor>) RuntimeCacheService.getInstance().getObject(MENU_UI_ACTORS);
    }

    public enum MenuUiComponentConstant {
        MENU_NOTIFICATION_MODAL, MENU_NOTIFICATION_INFO_LABEL, MENU_CLOSE_NOTIFICATION_BUTTON
    }

}
