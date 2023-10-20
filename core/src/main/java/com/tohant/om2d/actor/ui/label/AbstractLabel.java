package com.tohant.om2d.actor.ui.label;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Custom wrapper for {@link Label} actor.
 * Mostly, used to make default behaviour for LibGDXs {@link Label} actor, like
 * setting it's name, content text and skin.
 */
public abstract class AbstractLabel extends Label {

    public AbstractLabel(String id, String text, Skin skin) {
        super(text, skin);
        setName(id);
    }

}
