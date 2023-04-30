package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;

public class HallRoom extends Room {

    private final Type TYPE = Type.HALL;

    public Type getType() {
        return TYPE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
