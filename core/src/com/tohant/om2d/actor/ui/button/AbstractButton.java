package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tohant.om2d.command.AbstractCommand;

public abstract class AbstractButton extends Button {

    private final AbstractCommand command;

    public AbstractButton(String id, AbstractCommand command, Skin skin) {
        setName(id);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                command.execute();
                return false;
            }
        });
        this.command = command;
    }

    public AbstractCommand getCommand() {
        return command;
    }

}
