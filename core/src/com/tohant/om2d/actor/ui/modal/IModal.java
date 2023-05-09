package com.tohant.om2d.actor.ui.modal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public interface IModal {

    void toggle();

    void updateContentText(String text);
    void setModalData(ModalData modalData);

    void updatePosition(Vector2 position);

    default Window getThis() {
        return (Window) this;
    }

}
