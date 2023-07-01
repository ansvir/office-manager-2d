package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class ProgressEntity implements Serializable {

    private String id;
    private String companyId;
    private String officeId;

    public ProgressEntity(String id, String companyId, String officeId) {
        this.id = id;
        this.companyId = companyId;
        this.officeId = officeId;
    }

    public ProgressEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
