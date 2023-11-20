package com.tohant.om2d.actor.ui.button;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tohant.om2d.command.Command;
import lombok.Getter;

@Getter
public class GameTextButton extends TextButton {

    private final Command command;

    public GameTextButton(String id, Command command, String text, Skin skin) {
        super(text, skin);
        setName(id);
        this.command = command;
    }

}
