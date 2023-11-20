package com.tohant.om2d.actor.ui.pane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.GameTextButton;

public class DefaultPane extends AbstractPane {

    public DefaultPane(String id, Array<Actor> elements, Alignment alignment, String title, Skin skin) {
        super(id, elements, alignment, title, skin);
    }

}
