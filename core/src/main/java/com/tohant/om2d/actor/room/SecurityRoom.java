package com.tohant.om2d.actor.room;

import com.tohant.om2d.model.room.RoomInfo;

public class SecurityRoom extends Room {

    private static final Room.Type TYPE = Type.SECURITY;

    public SecurityRoom(String id, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
