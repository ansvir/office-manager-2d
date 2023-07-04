package com.tohant.om2d.actor.ui.label;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class AbstractLabel extends Label {

    public AbstractLabel(String id, String text, Skin skin) {
        super(text, skin);
        setName(id);
    }

}
