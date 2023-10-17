package com.tohant.om2d.model.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "CELL")
public class CellEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private int x;
    @DatabaseField
    private int y;
    @DatabaseField
    private String items;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "room_id")
    private RoomEntity roomEntity;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "level_id")
    private LevelEntity levelEntity;

    public CellEntity(UUID id, int x, int y, String items, RoomEntity roomEntity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.items = items;
        this.roomEntity = roomEntity;
    }

    public CellEntity(int x, int y, String items) {
        this.x = x;
        this.y = y;
        this.items = items;
    }

    public CellEntity(UUID id, int x, int y, String items, RoomEntity roomEntity, LevelEntity levelEntity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.items = items;
        this.roomEntity = roomEntity;
        this.levelEntity = levelEntity;
    }

    public CellEntity() {
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public LevelEntity getLevelEntity() {
        return levelEntity;
    }

    public void setLevelEntity(LevelEntity levelEntity) {
        this.levelEntity = levelEntity;
    }

}
