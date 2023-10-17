package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "PROGRESS")
public class ProgressEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "company_id")
    private CompanyEntity companyEntity;

    public ProgressEntity(CompanyEntity companyEntity) {
        super(null);
        this.companyEntity = companyEntity;
    }

    public ProgressEntity() {
        super(null);
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

}
