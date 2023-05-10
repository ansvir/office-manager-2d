package com.tohant.om2d.actor.ui.dropdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public class HorizontalDropdown extends Table {

    private TextButton triggerButton;
    private Table optionsTable;
    private boolean isExpanded;

    public HorizontalDropdown(float x, float y, Array<TextButton> options, Skin skin) {
        super(skin);
        triggerButton = new TextButton("<", skin);
        setPosition(x, y);
        optionsTable = new Table(skin);
        for (TextButton option : options) {
            optionsTable.add(option).pad(DEFAULT_PAD / 5f).width(200f).row();
        }
        add(optionsTable);
        triggerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
                if (!isExpanded) {
                    showOptions();
                    triggerButton.setText(">");
                } else {
                    hideOptions();
                    triggerButton.setText("<");
                }
                return false;
            }
        });
        add(triggerButton).bottom().padBottom(DEFAULT_PAD / 5f);
        hideOptions();

//        popupWindow.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//        });
    }

    private void showOptions() {
        isExpanded = true;
        optionsTable.setVisible(true);
    }

    private void hideOptions() {
        isExpanded = false;
        optionsTable.setVisible(false);
    }

}
