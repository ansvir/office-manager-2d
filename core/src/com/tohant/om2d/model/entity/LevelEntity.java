package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class LevelEntity implements Serializable {

    private String id;
    private long level;
    private Array<CellEntity> cellEntities;

    public LevelEntity(String id, long level, Array<CellEntity> cellEntities) {
        this.id = id;
        this.level = level;
        this.cellEntities = cellEntities;
    }

    public LevelEntity() {
        // serialization constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public Array<CellEntity> getCellEntities() {
        return cellEntities;
    }

    public void setCellEntities(Array<CellEntity> cellEntities) {
        this.cellEntities = cellEntities;
    }

}
