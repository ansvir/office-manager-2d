package com.tohant.om2d.actor.room;

import com.tohant.om2d.model.room.RoomInfo;

public class ElevatorRoom extends Room {

    private final Type type = Type.ELEVATOR;

    public ElevatorRoom(String id, RoomInfo roomInfo, float x, float y, float width, float height) {
        super(id, roomInfo, x, y, width, height);
    }

    @Override
    public Type getType() {
        return type;
    }

}
