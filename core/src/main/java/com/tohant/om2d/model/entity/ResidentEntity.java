package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "RESIDENT")
public class ResidentEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String businessName;
    @ForeignCollectionField(eager = true, columnName = "room_id")
    private Collection<RoomEntity> roomEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "office_id")
    private OfficeEntity officeEntity;

    public ResidentEntity(String businessName, OfficeEntity officeEntity) {
        this.businessName = businessName;
        this.officeEntity = officeEntity;
    }

    public ResidentEntity() {
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Collection<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }

    public void setRoomEntities(Collection<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }

    public OfficeEntity getOfficeEntity() {
        return officeEntity;
    }

    public void setOfficeEntity(OfficeEntity officeEntity) {
        this.officeEntity = officeEntity;
    }

}
