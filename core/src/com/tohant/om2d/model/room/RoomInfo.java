package com.tohant.om2d.model.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.TimeLineDate;

import java.util.UUID;

public class RoomInfo {

    private String id;
    private Array<Staff> staff;
    private String number;
    private float price;
    private float cost;
    private TimeLineDate buildTime;
    private Room.Type type;

    public RoomInfo(String id, Array<Staff> staff, float price, float cost, TimeLineDate buildTime, Room.Type type) {
        this.id = id;
        this.staff = staff;
        this.number = this.id.substring(0, 4);
        this.price = price;
        this.cost = cost;
        this.buildTime = buildTime;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public TimeLineDate getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(TimeLineDate buildTime) {
        this.buildTime = buildTime;
    }

    public Array<Staff> getStaff() {
        return staff;
    }

    public void setStaff(Array<Staff> staff) {
        this.staff = staff;
    }

    public Room.Type getType() {
        return type;
    }

    public void setType(Room.Type type) {
        this.type = type;
    }

}
