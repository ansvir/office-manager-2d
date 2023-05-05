package com.tohant.om2d.actor.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;

public class HallRoom extends Room {

    private final Type TYPE = Type.HALL;

    public HallRoom(String id, float price, float cost, float x, float y, float width, float height) {
        super(id, new Array<>(), price, cost, x, y, width, height);
    }

    public HallRoom(float price, float cost, float x, float y, float width, float height) {
        super(new Array<>(), price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getTexturePath() {
        return "hall.png";
    }

}
