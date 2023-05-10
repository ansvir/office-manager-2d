package com.tohant.om2d.model.task;

import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.room.RoomInfo;

import java.util.concurrent.CompletableFuture;

public class RoomBuildingModel {

    private CompletableFuture<Room> room;
    private RoomInfo roomInfo;

    public RoomBuildingModel(CompletableFuture<Room> room, RoomInfo roomInfo) {
        this.room = room;
        this.roomInfo = roomInfo;
    }

    public CompletableFuture<Room> getRoom() {
        return room;
    }

    public void setRoom(CompletableFuture<Room> room) {
        this.room = room;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
