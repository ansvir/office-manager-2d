package com.tohant.om2d.storage;

import com.tohant.om2d.actor.Room;

public class GameCache extends Cache {

    // main properties
    public static final String CURRENT_ROOM_TYPE = "CURR_ROOM_TYPE";
    public static final String CURRENT_BUDGET = "CURR_BUDGET";

    public Room.Type getRoomType() {
        String value = getValue(CURRENT_ROOM_TYPE);
        if (value == null) {
            return null;
        } else {
            return Room.Type.valueOf(value);
        }
    }

    public void setRoomType(Room.Type value) {
        setValue(CURRENT_ROOM_TYPE, value.name());
    }

    public float getBudget() {
        return getFloatValue(CURRENT_BUDGET);
    }

    public void setBudget(float budget) {
        setFloat(CURRENT_BUDGET, budget);
    }

}
