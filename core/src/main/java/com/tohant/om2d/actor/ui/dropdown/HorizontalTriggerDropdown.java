package com.tohant.om2d.actor.ui.dropdown;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tohant.om2d.actor.constant.Constant;
import com.tohant.om2d.actor.ui.list.AbstractList;

public class HorizontalTriggerDropdown extends AbstractDropDown {

    private final boolean simpleTriggerText;

    public HorizontalTriggerDropdown(String id, AbstractList options, Button triggerButton, TriggerButtonType triggerButtonType, boolean simpleTriggerText) {
        super(id, options, triggerButton);
        switch (triggerButtonType) {
            case RIGHT_BOTTOM: add(triggerButton).bottom().padLeft(Constant.DEFAULT_PAD / 5f); break;
            case LEFT_TOP: add(triggerButton).top().padRight(Constant.DEFAULT_PAD); getCells().reverse(); break;
        }
        this.simpleTriggerText = simpleTriggerText;
        hideOptions();
    }

    @Override
    public void showOptions() {
        setExpanded(true);
        getOptions().setVisible(true);
        if (getTriggerButton() instanceof TextButton && simpleTriggerText) {
            ((TextButton) getTriggerButton()).setText(">");
        }
    }

    @Override
    public void hideOptions() {
        setExpanded(false);
        getOptions().setVisible(false);
        if (getTriggerButton() instanceof TextButton && simpleTriggerText) {
            ((TextButton) getTriggerButton()).setText("<");
        }
    }

    public enum TriggerButtonType {
        LEFT_TOP, RIGHT_BOTTOM
    }

}
