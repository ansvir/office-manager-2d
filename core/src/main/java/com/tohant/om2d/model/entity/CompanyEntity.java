package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.CompanyDao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "COMPANY")
public class CompanyEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String name;
    @ForeignCollectionField(eager = true, columnName = "office_id")
    private Collection<OfficeEntity> officeEntities;
    @DatabaseField
    private Region region;

    public CompanyEntity(String id, String name, Collection<OfficeEntity> officeEntities, Region region) {
        super(id);
        this.name = name;
        this.officeEntities = officeEntities;
        this.region = region;
    }

    public CompanyEntity() {
        super(null);
        // serialization constructor
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

    public Collection<OfficeEntity> getOfficeEntities() {
        return officeEntities;
    }

    public void setOfficeEntities(List<OfficeEntity> officeEntities) {
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
