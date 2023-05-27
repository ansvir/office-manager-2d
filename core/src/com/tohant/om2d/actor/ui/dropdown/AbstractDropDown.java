package com.tohant.om2d.actor.ui.dropdown;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.list.AbstractList;

public abstract class AbstractDropDown extends Table implements ToggleActor {

    private boolean isExpanded;
    private final AbstractList options;
    private final AbstractTextButton triggerButton;

    public AbstractDropDown(String id, AbstractList options, AbstractTextButton triggerButton) {
        setName(id);
        this.options = options;
        add(options);
        this.triggerButton = triggerButton;
        triggerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
                toggle();
                return false;
            }
        });
    }

    public AbstractList getOptions() {
        return options;
    }

    public AbstractTextButton getTriggerButton() {
        return triggerButton;
    }

    protected void showOptions() {
        isExpanded = true;
        getOptions().setVisible(true);
        triggerButton.setText(">");
    }

    protected void hideOptions() {
        isExpanded = false;
        getOptions().setVisible(false);
        triggerButton.setText("<");
    }

    @Override
    public void toggle() {
        if (!isExpanded) {
            showOptions();
        } else {
            hideOptions();
        }
    }

}
