package com.tohant.om2d.model.task;

import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.room.RoomInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RoomBuildingModel {
    private final TimeLineTask<Room> timeLineTask;
    private final AsyncResult<Room> room;
    private final RoomInfo roomInfo;
}
