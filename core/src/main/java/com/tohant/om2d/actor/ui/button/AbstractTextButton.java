package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.cache.Cache.GAME_EXCEPTION;

public abstract class AbstractTextButton extends TextButton {

    public AbstractTextButton(String id, Command command, String text, Skin skin) {
        super(text, skin);
        setName(id);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    command.execute();
                } catch (GameException e) {
                    RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
                    Array<GameException> exceptions = (Array<GameException>) cacheService.getObject(GAME_EXCEPTION);
                    exceptions.add(e);
                }
                return false;
            }
        });
    }

}
