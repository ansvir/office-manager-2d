package com.tohant.om2d.model.entity;

import com.tohant.om2d.actor.ObjectCellItem;

import java.io.Serializable;

public class ObjectCellEntity implements Serializable {

    private String id;
    private ObjectCellItem objectCellItem;

    public ObjectCellEntity(String id, ObjectCellItem objectCellItem) {
        this.id = id;
        this.objectCellItem = objectCellItem;
    }

    public ObjectCellEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectCellItem getObjectCellItem() {
        return objectCellItem;
    }

    public void setObjectCellItem(ObjectCellItem objectCellItem) {
        this.objectCellItem = objectCellItem;
    }

}
