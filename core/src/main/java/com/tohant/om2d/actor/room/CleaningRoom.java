package com.tohant.om2d.actor.room;

import com.tohant.om2d.model.room.RoomInfo;

public class CleaningRoom extends Room {

    private static final Room.Type TYPE = Room.Type.CLEANING;

    public CleaningRoom(String id, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
