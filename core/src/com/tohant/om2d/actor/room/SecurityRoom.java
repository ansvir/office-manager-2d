package com.tohant.om2d.actor.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.model.room.RoomInfo;

public class SecurityRoom extends Room {

    private static final Room.Type TYPE = Type.SECURITY;

    public SecurityRoom(String id, RoomInfo roomInfo, float x, float y, float width, float height) {
        super(id, roomInfo, x, y, width, height);
    }

    public SecurityRoom(RoomInfo roomInfo, float x, float y, float width, float height) {
        super(roomInfo, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
