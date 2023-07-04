package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.tohant.om2d.storage.database.CompanyDao;
import com.tohant.om2d.storage.database.LevelDao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "LEVEL")
public class LevelEntity extends AbstractActorEntity {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private long level;
    @ForeignCollectionField(eager = true, columnName = "cell_id")
    private Collection<CellEntity> cellEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "office_id")
    private OfficeEntity officeEntity;

    public LevelEntity(String id, long level, List<CellEntity> cellEntities) {
        super(id);
        this.level = level;
        this.cellEntities = cellEntities;
    }

    public LevelEntity(String id, long level, Collection<CellEntity> cellEntities, OfficeEntity officeEntity) {
        super(id);
        this.level = level;
        this.cellEntities = cellEntities;
        this.officeEntity = officeEntity;
    }

    public LevelEntity() {
        super(null);
        // serialization constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public Collection<CellEntity> getCellEntities() {
        return cellEntities;
    }

    public void setCellEntities(List<CellEntity> cellEntities) {
        this.cellEntities = cellEntities;
    }

    public void setCellEntities(Collection<CellEntity> cellEntities) {
        this.cellEntities = cellEntities;
    }

    public OfficeEntity getOfficeEntity() {
        return officeEntity;
    }

    public void setOfficeEntity(OfficeEntity officeEntity) {
        this.officeEntity = officeEntity;
    }

}
