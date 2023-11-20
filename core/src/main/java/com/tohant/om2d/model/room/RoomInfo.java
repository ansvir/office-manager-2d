package com.tohant.om2d.model.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.actor.room.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoomInfo {

    private String id;
    private Array<Staff> staff;
    private String number;
    private float price;
    private float cost;
    private TimeLineDate buildTime;
    private Room.Type type;
    private boolean isUnderConstruction;

    public RoomInfo(String id, Array<Staff> staff, float price, float cost, TimeLineDate buildTime, Room.Type type) {
        this.id = id;
        this.staff = staff;
        this.number = UUID.randomUUID().toString().substring(0, 4);
        this.price = price;
        this.cost = cost;
        this.buildTime = buildTime;
        this.type = type;
    }

}
