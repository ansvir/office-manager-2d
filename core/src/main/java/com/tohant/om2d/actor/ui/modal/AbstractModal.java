package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.ui.button.GameTextButton;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public abstract class AbstractModal extends Window implements ToggleActor {

    public AbstractModal(String id, String title, Skin skin) {
        super(title, skin);
        setName(id);
        getTitleTable().getCells().get(0).pad(DEFAULT_PAD);
        getTitleTable().add(closeButton(skin)).right();
        setResizable(false);
        setMovable(true);
        /*if (modalData.getPosition() != null) {
            if (modalData.getPosition().x + this.getWidth() > Gdx.graphics.getWidth()) {
                super.setPosition(modalData.getPosition().x - this.getWidth(), modalData.getPosition().y - super.getPrefHeight());
            } else if (modalData.getPosition().y + this.getHeight() > Gdx.graphics.getHeight()) {
                super.setPosition(modalData.getPosition().x, modalData.getPosition().y - super.getPrefHeight());
            }
        }*/
    }

    @Override
    public void toggle() {
        this.setVisible(!this.isVisible());
    }

    @Override
    public void forceToggle(boolean value) {
        setVisible(value);
    }

    public void updateContentText(String targetId, String text) {
        getCells().forEach(c -> {
            if (c.getActor() != null && c.getActor().getName() != null
                    && c.getActor().getName().equals(targetId)) {
                ((Label) c.getActor()).setText(text);
            }
        });
        setSize(getPrefWidth(), getPrefHeight());
    }

    private GameTextButton closeButton(Skin skin) {
        return new GameTextButton(getName() + "_CLOSE_BUTTON", () -> forceToggle(false), "X", skin);
    }

}
