package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.room.RoomInfo;

public class RoomBuildingModel {

    private AsyncResult<Room> room;
    private RoomInfo roomInfo;

    public RoomBuildingModel(AsyncResult<Room> room, RoomInfo roomInfo) {
        this.room = room;
        this.roomInfo = roomInfo;
    }

    public AsyncResult<Room> getRoom() {
        return room;
    }

    public void setRoom(AsyncResult<Room> room) {
        this.room = room;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
