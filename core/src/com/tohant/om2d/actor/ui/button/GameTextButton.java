package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tohant.om2d.command.AbstractCommand;

public class GameTextButton extends AbstractTextButton {

    public GameTextButton(String id, AbstractCommand command, String text, Skin skin) {
        super(id, command, text, skin);
    }

}
