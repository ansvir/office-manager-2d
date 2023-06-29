package com.tohant.om2d.actor.ui.dropdown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;
import com.tohant.om2d.actor.ui.button.GameTextButton;
import com.tohant.om2d.actor.ui.label.GameLabel;
import com.tohant.om2d.actor.ui.list.AbstractList;
import com.tohant.om2d.common.storage.Command;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class VerticalInputDropDown extends AbstractDropDown {

    private static final String CURRENT_SELECTED_LABEL = "CURRENT_SELECTED_LABEL";

    public VerticalInputDropDown(String id, AbstractList options) {
        super(id, options, new GameTextButton(CURRENT_SELECTED_LABEL, () -> {},
                ((GameTextButton) options.getElements().get(0)).getText().toString(), getDefaultSkin()));
        getOptions().getElements().iterator().forEach(a -> a.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((GameTextButton) getTriggerButton()).setText(((GameTextButton) a).getText().toString());
                toggle();
                return false;
            }
        }));
        add(getTriggerButton()).padRight(DEFAULT_PAD);
        getCells().reverse();
        getCells().get(0).top();
        hideOptions();
    }

    @Override
    public void showOptions() {
        setExpanded(true);
        getOptions().setVisible(true);
    }

    @Override
    public void hideOptions() {
        setExpanded(false);
        getOptions().setVisible(false);
    }

}
