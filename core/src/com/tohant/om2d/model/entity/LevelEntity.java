package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class LevelEntity implements Serializable {

    private String id;
    private long level;
    private Array<String> cellsIds;

    public LevelEntity(String id, long level, Array<String> cellsIds) {
        this.id = id;
        this.level = level;
        this.cellsIds = cellsIds;
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

    public Array<String> getCellsIds() {
        return cellsIds;
    }

    public void setCellsIds(Array<String> cellsIds) {
        this.cellsIds = cellsIds;
    }

}
