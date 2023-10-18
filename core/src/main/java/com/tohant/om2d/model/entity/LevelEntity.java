package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;

@DatabaseTable(tableName = "LEVEL")
public class LevelEntity implements Serializable {

    @DatabaseField(generatedId = true)
    private UUID id;
    @DatabaseField
    private long level;
    @ForeignCollectionField(eager = true, columnName = "cell_id")
    private Collection<CellEntity> cellEntities;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "office_id")
    private OfficeEntity officeEntity;

    public LevelEntity(long level, List<CellEntity> cellEntities) {
        this.level = level;
        this.cellEntities = cellEntities;
    }

    public LevelEntity(long level, Collection<CellEntity> cellEntities, OfficeEntity officeEntity) {
        this.level = level;
        this.cellEntities = cellEntities;
        this.officeEntity = officeEntity;
    }

    public LevelEntity() {
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

    public static LevelEntity createEmpty() {
        int level = 0;
        List<CellEntity> cellEntities = IntStream.range(0, GRID_HEIGHT).boxed()
                .flatMap(r -> IntStream.range(0, GRID_WIDTH).boxed()
                        .map(c -> new CellEntity(r, c, null)))
                .collect(Collectors.toList());
        return new LevelEntity(level, cellEntities);
    }

}
