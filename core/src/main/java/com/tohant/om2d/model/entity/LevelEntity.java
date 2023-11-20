package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;

@Getter
@Setter
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

    public static LevelEntity createEmpty() {
        int level = 0;
        List<CellEntity> cellEntities = IntStream.range(0, GRID_HEIGHT).boxed()
                .flatMap(r -> IntStream.range(0, GRID_WIDTH).boxed()
                        .map(c -> new CellEntity(r, c, null)))
                .collect(Collectors.toList());
        return new LevelEntity(level, cellEntities);
    }

}
