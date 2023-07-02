package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class CompanyEntity implements Serializable {

    private String id;
    private String name;
    private Array<OfficeEntity> officeEntities;
    private Region region;

    public CompanyEntity(String id, String name, Array<OfficeEntity> officeEntities, Region region) {
        this.id = id;
        this.name = name;
        this.officeEntities = officeEntities;
        this.region = region;
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

    public Array<OfficeEntity> getOfficeEntities() {
        return officeEntities;
    }

    public void setOfficeEntities(Array<OfficeEntity> officeEntities) {
        this.officeEntities = officeEntities;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public enum Region {
        EUROPE, AMERICA, ASIA, AFRICA, AUSTRALIA, OCEANIA
    }

}
