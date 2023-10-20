package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

/**
 * Modal window
 */
public abstract class AbstractModal extends Window implements ToggleActor {

    protected static final String CONTENT_NAME = "CONTENT";
    protected static final String ACTOR_N = "ACTOR#%s";

    public AbstractModal(String id, String title, AbstractTextButton close, Skin skin) {
        super(title, skin);
        setName(id);
        getTitleTable().getCells().get(0).pad(DEFAULT_PAD);
        getTitleTable().add(close).right();
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

    @Deprecated
    public void setModalData(ModalData modalData) {
        getTitleLabel().setText(modalData.getTitle());
//        updateContentText(modalData.getText());
        updateActors(modalData);
        this.setSize(getPrefWidth(), getPrefHeight());
    }

    public void updatePosition(Vector2 position) {
        this.setPosition(position.x, position.y);
    }

    @Deprecated
    private void updateActors(ModalData modalData) {
        for (int i = 0; i < modalData.getActors().size; i++) {
            int iCopy = i;
            getCells().forEach(c -> {
                if (c.getActor() != null && c.getActor().getName() != null && c.getActor().getName().equals(String.format(ACTOR_N, iCopy))) {
                    c.setActor(modalData.getActors().get(iCopy));
                }
            });
        }
    }

}
