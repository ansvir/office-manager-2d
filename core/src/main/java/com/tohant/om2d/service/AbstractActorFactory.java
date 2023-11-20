package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.storage.cache.GameCache;

import static com.tohant.om2d.storage.cache.GameCache.GAME_EXCEPTION;

public abstract class AbstractActorFactory {

    public abstract Array<Actor> getGameActors();

    public void postInit(GameCache gameCache) {
        getGameActors().select(a -> a instanceof GameTextButton).forEach(tb -> tb.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    ((GameTextButton) tb).getCommand().execute();
                } catch (GameException e) {
                    Array<GameException> exceptions = (Array<GameException>) gameCache.getObject(GAME_EXCEPTION);
                    exceptions.add(e);
                }
                return false;
            }
        }));
    }

}
