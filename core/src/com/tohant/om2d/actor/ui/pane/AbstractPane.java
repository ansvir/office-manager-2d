package com.tohant.om2d.actor.ui.pane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.ui.button.AbstractTextButton;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public abstract class AbstractPane extends Window implements ToggleActor {

    private boolean isCollapsed;
    private final Array<Actor> elements;
    private final AbstractTextButton collapse;
    private final Alignment alignment;

    public AbstractPane(String id, Array<Actor> elements, AbstractTextButton collapse, Alignment alignment, String title, Skin skin) {
        super(title, skin);
        setName(id);
        isCollapsed = false;
        this.elements = elements;
        this.collapse = collapse;
        this.alignment = alignment;
        getTitleTable().add(collapse).pad(DEFAULT_PAD).right();
        getTitleTable().getCells().get(0).pad(DEFAULT_PAD);
        setMovable(false);
        setResizable(false);
        for (Actor e : this.elements) {
            add(e).pad(DEFAULT_PAD).grow();
        }
        switch (this.alignment) {
            case BOTTOM: {
                this.align(Align.left);
                setWidth(Gdx.graphics.getWidth());
                setHeight(Gdx.graphics.getHeight() / 7f);
            } break;
        }
    }

    public enum Alignment {
        LEFT, TOP, RIGHT, BOTTOM
    }

    @Override
    public void toggle() {
        switch (this.alignment) {
            case BOTTOM: {
                if (isCollapsed) {
                    this.collapse.setText("-");
                    setHeight(Gdx.graphics.getHeight() / 7f);
                    setTouchable(Touchable.enabled);
                } else {
                    this.collapse.setText("+");
                    setHeight(Gdx.graphics.getHeight() / 21f);
                    setTouchable(Touchable.childrenOnly);
                }
                break;
            }
        }
        isCollapsed = !isCollapsed;
    }
}
