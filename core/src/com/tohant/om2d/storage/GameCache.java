package com.tohant.om2d.storage;

import com.tohant.om2d.actor.Room;

public class GameCache extends Cache {

    // main properties
    public static final String CURRENT_ROOM_TYPE = "CURR_ROOM_TYPE";
    public static final String CURRENT_BUDGET = "CURR_BUDGET";
    public static final String CURRENT_TIME = "CURR_TIME";
    public static final String OFFICES_AMOUNT = "OFFICES_AM";
    public static final String HALLS_AMOUNT = "HALLS_AM";
    public static final String SECURITY_AMOUNT = "SECURITY_AM";

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

    public void setTime(String time) {
        setValue(CURRENT_TIME, time);
    }

    public String getTime() {
        return getValue(CURRENT_TIME);
    }

    public void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE: setLong(OFFICES_AMOUNT, amount); break;
            case HALL: setLong(HALLS_AMOUNT, amount); break;
            case SECURITY: setLong(SECURITY_AMOUNT, amount); break;
            default: break;
        }
    }

    public long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE: return getLong(OFFICES_AMOUNT);
            case HALL: return getLong(HALLS_AMOUNT);
            case SECURITY: return getLong(SECURITY_AMOUNT);
            default: return -1L;
        }
    }

}
