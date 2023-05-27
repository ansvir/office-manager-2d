package com.tohant.om2d.storage.entity;

import java.io.Serializable;

public class StaffEntity implements Serializable {

    private String id;
    private String fullName;
    private float salary;
    private String type;
    private RoomEntity room;

    public StaffEntity(String id, String fullName, float salary, String type, RoomEntity room) {
        this.id = id;
        this.fullName = fullName;
        this.salary = salary;
        this.type = type;
        this.room = room;
    }

    public StaffEntity() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

}
