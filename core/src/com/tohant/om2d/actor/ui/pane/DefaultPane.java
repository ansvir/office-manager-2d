package com.tohant.om2d.actor.ui.pane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;

public class DefaultPane extends AbstractPane {

    public DefaultPane(String id, Array<Actor> elements, AbstractTextButton collapse, Alignment alignment, String title, Skin skin) {
        super(id, elements, collapse, alignment, title, skin);
    }

}
