package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class DefaultModal extends Window implements IModal {

    private static final String CONTENT_NAME = "CONTENT";
    private static final String ACTOR_N = "ACTOR#%s";

    private final Skin skin;

    public DefaultModal(ModalData modalData) {
        super(modalData.getTitle(), getDefaultSkin());
        super.getTitleTable().getCells().get(0).pad(DEFAULT_PAD);
        this.skin = getDefaultSkin();
        TextButton closeButton = getCloseButton();
        super.getTitleTable().add(closeButton).right();
        super.setVisible(false);
        super.setResizable(false);
        super.setMovable(true);
        super.setSize(getPrefWidth(), getPrefHeight());
        if (modalData.getPosition() != null) {
            if (modalData.getPosition().x + this.getWidth() > Gdx.graphics.getWidth()) {
                super.setPosition(modalData.getPosition().x - this.getWidth(), modalData.getPosition().y - super.getPrefHeight());
            } else if (modalData.getPosition().y + this.getHeight() > Gdx.graphics.getHeight()) {
                super.setPosition(modalData.getPosition().x, modalData.getPosition().y - super.getPrefHeight());
            }
        }
        Label text = new Label(modalData.getText(), skin);
        text.setName(CONTENT_NAME);
        super.add(text).pad(DEFAULT_PAD).center().expand();
        super.row();
        for (int i = 0; i < modalData.getActors().size; i++) {
            Actor a = modalData.getActors().get(i);
            a.setName(String.format(ACTOR_N, i));
            this.add(a).pad(DEFAULT_PAD).center().expand();
            this.row();
        }
    }

    @Override
    public void toggle() {
        this.setVisible(!this.isVisible());
    }

    @Override
    public void updateContentText(String text) {
        this.getCells().forEach(c -> {
            if (c.getActor() != null && c.getActor().getName() != null && c.getActor().getName().equals(CONTENT_NAME)) {
                ((Label) c.getActor()).setText(text);
            }
        });
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }

    @Override
    public void setModalData(ModalData modalData) {
        this.getTitleLabel().setText(modalData.getTitle());
        updateContentText(modalData.getText());
        updateActors(modalData);
        this.setSize(this.getPrefWidth(), this.getPrefHeight());
    }

    @Override
    public void updatePosition(Vector2 position) {
        this.setPosition(position.x, position.y);
    }

    private TextButton getCloseButton() {
        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                setVisible(false);
                return false;
            }
        });
        return closeButton;
    }

    private void updateActors(ModalData modalData) {
        for (int i = 0; i < modalData.getActors().size; i++) {
            int iCopy = i;
            this.getCells().forEach(c -> {
                if (c.getActor() != null && c.getActor().getName() != null && c.getActor().getName().equals(String.format(ACTOR_N, iCopy))) {
                    c.setActor(modalData.getActors().get(iCopy));
                }
            });
        }
    }

}
