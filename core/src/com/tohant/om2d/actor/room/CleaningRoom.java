package com.tohant.om2d.actor.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;

public class CleaningRoom extends Room {

    private static final Room.Type TYPE = Room.Type.CLEANING;

    public CleaningRoom(String id, Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        super(id, staff, price, cost, x, y, width, height);
    }

    public CleaningRoom(Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        super(staff, price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
