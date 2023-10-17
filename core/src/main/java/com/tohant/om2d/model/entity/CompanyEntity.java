package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "COMPANY")
public class CompanyEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String name;
    @ForeignCollectionField(eager = true, columnName = "office_id")
    private Collection<OfficeEntity> officeEntities;


    public CompanyEntity(String name, Collection<OfficeEntity> officeEntities) {
        this.name = name;
        this.officeEntities = officeEntities;
    }

    public CompanyEntity() {
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

}
