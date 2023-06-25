package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class ResidentEntity implements Serializable {

    private String id;
    private String businessName;
    private Array<String> workersIds;

    public ResidentEntity(String id, String businessName, Array<String> workersIds) {
        this.id = id;
        this.businessName = businessName;
        this.workersIds = workersIds;
    }

    public ResidentEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Array<String> getWorkersIds() {
        return workersIds;
    }

    public void setWorkersIds(Array<String> workersIds) {
        this.workersIds = workersIds;
    }

}
