package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "WORKER")
public class WorkerEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String name;
    @DatabaseField
    private float mood;
    @DatabaseField
    private String type;
    @DatabaseField
    private float salary;
    @DatabaseField(foreign = true, foreignAutoRefresh = true,  columnName = "room_id")
    private RoomEntity roomEntity;

    public WorkerEntity(String name, float mood, String type, float salary) {
        this.name = name;
        this.mood = mood;
        this.type = type;
        this.salary = salary;
    }

    public WorkerEntity(String name, float mood, String type, float salary, RoomEntity roomEntity) {
        this.name = name;
        this.mood = mood;
        this.type = type;
        this.salary = salary;
        this.roomEntity = roomEntity;
    }

    public WorkerEntity() {
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMood() {
        return mood;
    }

    public void setMood(float mood) {
        this.mood = mood;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

}
