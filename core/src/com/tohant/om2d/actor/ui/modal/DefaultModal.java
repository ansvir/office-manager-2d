package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.label.GameLabel;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class DefaultModal extends AbstractModal {

    public DefaultModal(String id, String title, Array<Actor> children, AbstractTextButton close, Skin skin) {
        super(id, title, close, skin);
        for (Actor c : children) {
            if (c instanceof GameLabel) {
                add(c).pad(DEFAULT_PAD).center().expand();
                row();
            }
        }
        for (Actor c : children) {
            if (c instanceof GameTextButton) {
                add(c).pad(DEFAULT_PAD).center().expand();
                row();
            }
        }
    }

}
