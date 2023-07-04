package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tohant.om2d.command.Command;

public abstract class AbstractButton extends Button {

    public AbstractButton(String id, Command command, Skin skin) {
        super(skin);
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
