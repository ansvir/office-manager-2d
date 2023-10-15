package com.tohant.om2d.actor.ui.slide;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.util.AssetsUtil;
import com.tohant.om2d.actor.ui.button.GameTextButton;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;

/**
 * Slideshow that has "slides", i.e. other UI actors inside and change from first to second etc. one by one.
 * Rule is that slideshow controlled only by trigger(s) that change actor, placed in array on {@link currentIndex},
 * decrementing it or incrementing by one.
 */
public abstract class AbstractSlideShow extends Table implements ToggleActor {

    private int currentIndex;
    private final Array<Actor> actors;

    public AbstractSlideShow(String id, Array<Actor> actors) {
        setName(id);
        this.actors = actors;
        this.currentIndex = 0;
        createCarcass();
    }

    private void createCarcass() {
        add(new GameTextButton(getName() + "_LEFT_BUTTON", () -> {
            currentIndex = currentIndex - 1 < 0 ? actors.size - 1 : currentIndex - 1;
            getCells().get(1).setActor(actors.get(currentIndex));
        }, "<", AssetsUtil.getDefaultSkin())).pad(DEFAULT_PAD).center();
        add(actors.get(currentIndex)).pad(DEFAULT_PAD).center();
        add(new GameTextButton(getName() + "_RIGHT_BUTTON", () -> {
            currentIndex = currentIndex + 1 >= actors.size ? 0 : currentIndex + 1;
            getCells().get(1).setActor(actors.get(currentIndex));
        }, ">", AssetsUtil.getDefaultSkin())).pad(DEFAULT_PAD).center();
    }

    @Override
    public void toggle() {
        setVisible(!isVisible());
    }

    @Override
    public void forceToggle(boolean value) {
        setVisible(value);
    }

}
