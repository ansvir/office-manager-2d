package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class CompanyEntity implements Serializable {

    private String id;
    private String name;
    private Array<String> officesIds;

    public CompanyEntity(String id, String name, Array<String> officesIds) {
        this.id = id;
        this.name = name;
        this.officesIds = officesIds;
    }

    public CompanyEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Array<String> getOfficesIds() {
        return officesIds;
    }

    public void setOfficesIds(Array<String> officesIds) {
        this.officesIds = officesIds;
    }
    
}
