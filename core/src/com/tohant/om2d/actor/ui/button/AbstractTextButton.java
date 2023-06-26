package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tohant.om2d.common.storage.Command;

public abstract class AbstractTextButton extends TextButton {

    public AbstractTextButton(String id, Command command, String text, Skin skin) {
        super(text, skin);
        setName(id);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                command.execute();
                return false;
            }
        });
    }

}
