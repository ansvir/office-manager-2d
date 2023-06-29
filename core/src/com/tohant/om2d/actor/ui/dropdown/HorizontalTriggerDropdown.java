package com.tohant.om2d.actor.ui.dropdown;

import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.list.AbstractList;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class HorizontalTriggerDropdown extends AbstractDropDown {

    public HorizontalTriggerDropdown(String id, AbstractList options, AbstractTextButton triggerButton) {
        super(id, options, triggerButton);
        add(triggerButton).bottom().padLeft(DEFAULT_PAD / 5f);
        hideOptions();
    }

    @Override
    public void showOptions() {
        setExpanded(true);
        getOptions().setVisible(true);
        ((AbstractTextButton) getTriggerButton()).setText(">");
    }

    @Override
    public void hideOptions() {
        setExpanded(false);
        getOptions().setVisible(false);
        ((AbstractTextButton) getTriggerButton()).setText("<");
    }

}
