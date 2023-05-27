package com.tohant.om2d.actor.ui.dropdown;

import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.list.AbstractList;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class HorizontalDropdown extends AbstractDropDown {

    public HorizontalDropdown(String id, AbstractList options, AbstractTextButton triggerButton) {
        super(id, options, triggerButton);
        add(triggerButton).bottom().padBottom(DEFAULT_PAD / 5f);
        hideOptions();
    }

}
