package com.tohant.om2d.model.entity;

import java.io.Serializable;

public class ProgressEntity implements Serializable {

    private String id;
    private CompanyEntity companyEntity;
    private OfficeEntity officeEntity;
    private LevelEntity levelEntity;

    public ProgressEntity(String id, CompanyEntity companyEntity, OfficeEntity officeEntity, LevelEntity levelEntity) {
        this.id = id;
        this.companyEntity = companyEntity;
        this.officeEntity = officeEntity;
        this.levelEntity = levelEntity;
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

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public OfficeEntity getOfficeEntity() {
        return officeEntity;
    }

    public void setOfficeEntity(OfficeEntity officeEntity) {
        this.officeEntity = officeEntity;
    }

    public LevelEntity getLevelEntity() {
        return levelEntity;
    }

    public void setLevelEntity(LevelEntity levelEntity) {
        this.levelEntity = levelEntity;
    }

}
