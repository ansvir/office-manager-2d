package com.tohant.om2d.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.Cache;

import static com.tohant.om2d.storage.Cache.GAME_EXCEPTION;

public class AbstractStage extends Stage {

    private final RuntimeCacheService cacheService;
    public AbstractStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        cacheService = RuntimeCacheService.getInstance();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean childHandled = false;
        Actor actor = hit(screenX, screenY, false);
        if (actor != null) {
            try {
                childHandled = actor.fire(new InputEvent());
            } catch (RuntimeException ignored) {

            }
        }
        boolean superTouchDown = false;
        try {
            superTouchDown = super.touchDown(screenX, screenY, pointer, button);
        } catch (RuntimeException ignored) {

        }
        return childHandled || superTouchDown;
    }

    public void checkForExceptionsAndThrowIfExist(int i) {
        Array<GameException> exceptions = (Array<GameException>) cacheService.getObject(GAME_EXCEPTION);
        if (exceptions.size > 0 && i < exceptions.size) {
            checkForExceptionsAndThrowIfExist(i + 1);
            GameException e = exceptions.get(i);
            exceptions.removeIndex(i);
            throw e;
        }
    }

}
