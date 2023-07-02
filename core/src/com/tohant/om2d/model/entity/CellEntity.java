package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class CellEntity implements Serializable {

    private String id;
    private String roomId;
    private String roomType;
    private int x;
    private int y;
    private Array<ObjectCellEntity> objectCellEntities;

    public CellEntity(String id, String roomId, String roomType, int x, int y, Array<ObjectCellEntity> objectCellEntities) {
        this.id = id;
        this.roomId = roomId;
        this.roomType = roomType;
        this.x = x;
        this.y = y;
        this.objectCellEntities = objectCellEntities;
    }

    public CellEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public Array<ObjectCellEntity> getObjectCellEntities() {
        return objectCellEntities;
    }

    public void setObjectCellEntities(Array<ObjectCellEntity> objectCellEntities) {
        this.objectCellEntities = objectCellEntities;
    }

}
