package com.tohant.om2d.actor.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;

public class OfficeRoom extends Room {

    private final Type TYPE = Type.OFFICE;

    public OfficeRoom(String id, Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        super(id, staff, price, cost, x, y, width, height);
    }

    public OfficeRoom(Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        super(staff, price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
