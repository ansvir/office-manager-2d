package com.tohant.om2d.actor.ui.label;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Usually used independently of other actors.
 */
public class GameStandaloneLabel extends AbstractLabel {

    public GameStandaloneLabel(String id, String text, Skin skin) {
        super(id, text, skin);
        setFontScale(1.5f);
    }

}
