package com.tohant.om2d.actor.ui.list;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

public abstract class AbstractList extends Table {

    private final Array<Actor> elements;
    private int selected;

    public AbstractList(String id, Array<Actor> elements) {
        setName(id);
        for (int i = 0; i < elements.size; i++) {
            if (i < elements.size - 1) {
                add(elements.get(i)).padBottom(DEFAULT_PAD / 5f).growX().row();
            } else {
                add(elements.get(i)).growX();
            }
        }
        this.elements = elements;
        this.selected = -1;
    }

    public Actor getCurrent() {
        return this.elements.get(this.selected);
    }

    public Array<Actor> getElements() {
        return elements;
    }

    public void select(Actor component) {
        for (int i = 0; i < this.elements.size; i++) {
            if (this.elements.get(i).getName().equals(component.getName())) {
                this.selected = i;
                break;
            }
        }
    }

}
