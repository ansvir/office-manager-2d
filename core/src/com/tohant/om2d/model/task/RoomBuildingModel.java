package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.room.RoomInfo;

import java.util.concurrent.CompletableFuture;

public class RoomBuildingModel {

    private TimeLineTask<Room> timeLineTask;
    private AsyncResult<Room> room;
    private RoomInfo roomInfo;

    public RoomBuildingModel(TimeLineTask<Room> timeLineTask, AsyncResult<Room> room, RoomInfo roomInfo) {
        this.timeLineTask = timeLineTask;
        this.room = room;
        this.roomInfo = roomInfo;
    }

    public TimeLineTask<Room> getTimeLineTask() {
        return timeLineTask;
    }

    public void setTimeLineTask(TimeLineTask<Room> timeLineTask) {
        this.timeLineTask = timeLineTask;
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
