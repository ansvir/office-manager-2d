package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "OFFICE")
public class OfficeEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String name;
    @DatabaseField
    private float popularity;
    @DatabaseField
    private float budget;
    @ForeignCollectionField(eager = true, columnName = "level_id")
    private Collection<LevelEntity> levelEntities;
    @ForeignCollectionField(eager = true, columnName = "resident_id")
    private Collection<ResidentEntity> residentEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "company_id")
    private CompanyEntity companyEntity;

    public OfficeEntity(String id, String name, float popularity, float budget, List<LevelEntity> levelEntities, List<ResidentEntity> residentEntities) {
        super(id);
        this.name = name;
        this.popularity = popularity;
        this.budget = budget;
        this.levelEntities = levelEntities;
        this.residentEntities = residentEntities;
    }

    public OfficeEntity(String id, String name, float popularity, float budget, Collection<LevelEntity> levelEntities, Collection<ResidentEntity> residentEntities, CompanyEntity companyEntity) {
        super(id);
        this.name = name;
        this.popularity = popularity;
        this.budget = budget;
        this.levelEntities = levelEntities;
        this.residentEntities = residentEntities;
        this.companyEntity = companyEntity;
    }

    public OfficeEntity() {
        super(null);
        // for serialization
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public Collection<LevelEntity> getLevelEntities() {
        return levelEntities;
    }

    public void setLevelEntities(List<LevelEntity> levelEntities) {
        this.levelEntities = levelEntities;
    }

    public Collection<ResidentEntity> getResidentEntities() {
        return residentEntities;
    }

    public void setResidentEntities(List<ResidentEntity> residentEntities) {
        this.residentEntities = residentEntities;
    }

    public void setLevelEntities(Collection<LevelEntity> levelEntities) {
        this.levelEntities = levelEntities;
    }

    public void setResidentEntities(Collection<ResidentEntity> residentEntities) {
        this.residentEntities = residentEntities;
    }

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }
}
