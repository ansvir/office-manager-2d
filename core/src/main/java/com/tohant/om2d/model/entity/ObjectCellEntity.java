package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "OBJECT_CELL")
public class ObjectCellEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private String item;
    @DatabaseField(foreign = true, columnName = "cell_id", foreignAutoRefresh = true, foreignAutoCreate = true)
    private CellEntity cellEntity;

    public ObjectCellEntity(String id, String item) {
        super(id);
        this.item = item;
    }

    public ObjectCellEntity(String id, String item, CellEntity cellEntity) {
        super(id);
        this.item = item;
        this.cellEntity = cellEntity;
    }

    public ObjectCellEntity() {
        super(null);
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public CellEntity getCellEntity() {
        return cellEntity;
    }

    public void setCellEntity(CellEntity cellEntity) {
        this.cellEntity = cellEntity;
    }

}
