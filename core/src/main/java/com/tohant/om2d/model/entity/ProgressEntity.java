package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "PROGRESS")
public class ProgressEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "company_id")
    private CompanyEntity companyEntity;

    @DatabaseField
    private String currentOfficeId;

    @DatabaseField
    private String currentLevelId;

    public ProgressEntity(CompanyEntity companyEntity, String currentOfficeId, String currentLevelId) {
        this.companyEntity = companyEntity;
        this.currentOfficeId = currentOfficeId;
        this.currentLevelId = currentLevelId;
    }

    public ProgressEntity() {
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }

    public String getCurrentOfficeId() {
        return currentOfficeId;
    }

    public void setCurrentOfficeId(String currentOfficeId) {
        this.currentOfficeId = currentOfficeId;
    }

    public String getCurrentLevelId() {
        return currentLevelId;
    }

    public void setCurrentLevelId(String currentLevelId) {
        this.currentLevelId = currentLevelId;
    }
}
