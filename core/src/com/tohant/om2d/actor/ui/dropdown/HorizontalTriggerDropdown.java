package com.tohant.om2d.actor.ui.dropdown;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tohant.om2d.actor.ui.list.AbstractList;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class HorizontalTriggerDropdown extends AbstractDropDown {

    public HorizontalTriggerDropdown(String id, AbstractList options, Button triggerButton, TriggerButtonType triggerButtonType) {
        super(id, options, triggerButton);
        switch (triggerButtonType) {
            case RIGHT_BOTTOM: add(triggerButton).bottom().padLeft(DEFAULT_PAD / 5f); break;
            case LEFT_TOP: add(triggerButton).top().padRight(DEFAULT_PAD); getCells().reverse(); break;
        }
        hideOptions();
    }

    @Override
    public void showOptions() {
        setExpanded(true);
        getOptions().setVisible(true);
        if (getTriggerButton() instanceof TextButton) {
            ((TextButton) getTriggerButton()).setText(">");
        }
    }

    @Override
    public void hideOptions() {
        setExpanded(false);
        getOptions().setVisible(false);
        if (getTriggerButton() instanceof TextButton) {
            ((TextButton) getTriggerButton()).setText("<");
        }
    }

    public enum TriggerButtonType {
        LEFT_TOP, RIGHT_BOTTOM
    }

}
