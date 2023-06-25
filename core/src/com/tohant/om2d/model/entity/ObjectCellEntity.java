package com.tohant.om2d.model.entity;

import java.io.Serializable;

public class ObjectCellEntity implements Serializable {

    private String id;
    private String itemId;

    public ObjectCellEntity(String id, String itemId) {
        this.id = id;
        this.itemId = itemId;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
