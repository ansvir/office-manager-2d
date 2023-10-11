package com.tohant.om2d.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.command.ui.CreateNotificationCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;

public class AbstractStage extends Stage {

    private final RuntimeCacheService cacheService;
    private final Array<GameException> exceptions;

    public AbstractStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        cacheService = RuntimeCacheService.getInstance();
        exceptions = (Array<GameException>) cacheService.getObject(Cache.GAME_EXCEPTION);
    }

    @Override
    public void act(float delta) {
        try {
            super.act(delta);
            checkForExceptionsAndThrowIfExist(0);
        } catch (Exception e) {
            if (e instanceof GameException && this instanceof UiStage) {
                UiActorService uiActorService = UiActorService.getInstance();
                getRoot().removeActor(uiActorService.getActorById(UiActorService.UiComponentConstant.NOTIFICATION_MODAL.name()));
                new CreateNotificationCommand((GameException) e).execute();
                addActor(uiActorService.getActorById(UiActorService.UiComponentConstant.NOTIFICATION_MODAL.name()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, false);
        if (actor != null) {
            try {
                childHandled = actor.fire(new InputEvent());
            } catch (RuntimeException e) {
                if (e instanceof GameException) {
                    exceptions.add((GameException) e);
                }
            }
        }
        boolean superTouchDown = false;
        try {
            superTouchDown = super.touchDown(screenX, screenY, pointer, button);
        } catch (RuntimeException e) {
            if (e instanceof GameException) {
                exceptions.add((GameException) e);
            }
        }
        return childHandled || superTouchDown;
    }

    public void checkForExceptionsAndThrowIfExist(int i) {
        if (exceptions.size > 0 && i < exceptions.size) {
            checkForExceptionsAndThrowIfExist(i + 1);
            GameException e = exceptions.get(i);
            exceptions.removeIndex(i);
            throw e;
        }
    }

}
