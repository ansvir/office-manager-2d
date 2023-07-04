package com.tohant.om2d.actor.room;

import com.tohant.om2d.model.room.RoomInfo;

public class HallRoom extends Room {

    private final Type TYPE = Type.HALL;

    public HallRoom(String id, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
