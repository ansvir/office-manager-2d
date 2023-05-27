package com.tohant.om2d.actor.room;

import com.tohant.om2d.model.room.RoomInfo;

public class CaffeRoom extends Room {

    private final Room.Type TYPE = Type.CAFFE;

    public CaffeRoom(String id, RoomInfo roomInfo, float x, float y, float width, float height) {
        super(id, roomInfo, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
