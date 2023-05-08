package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncTask;
import com.tohant.om2d.actor.room.Room;

public class RoomBuildTask implements AsyncTask<Room> {

    private Room room;
    private long time;



    public RoomBuildTask(Room room) {
        this.room = room;
    }

    @Override
    public Room call() throws Exception {
        return room;
    }

}
