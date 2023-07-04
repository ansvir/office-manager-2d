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
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "office_id")
    private OfficeEntity officeEntity;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = "level_id")
    private LevelEntity levelEntity;

    public ProgressEntity(String id, CompanyEntity companyEntity, OfficeEntity officeEntity, LevelEntity levelEntity) {
        super(id);
        this.companyEntity = companyEntity;
        this.officeEntity = officeEntity;
        this.levelEntity = levelEntity;
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
