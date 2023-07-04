package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class Office extends Group {

    public Office(String id, Array<Grid> levels) {
        setName(id);
//        for (int i = 0; i < levels.size; i++) {
//            levels.get(i).setName(LEVEL_PREFIX + i);
//            Grid level = levels.get(i);
//            level.setPosition(getX(), getY());
//            addActor(level);
//        }
        for (int i = 0; i < levels.size; i++) {
            addActor(levels.get(i));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
