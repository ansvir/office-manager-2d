package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class OfficeEntity implements Serializable {

    private String id;
    private String name;
    private float popularity;
    private float budget;
    private Array<LevelEntity> levelEntities;
    private Array<ResidentEntity> residentEntities;

    public OfficeEntity(String id, String name, float popularity, float budget, Array<LevelEntity> levelEntities, Array<ResidentEntity> residentEntities) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.budget = budget;
        this.levelEntities = levelEntities;
        this.residentEntities = residentEntities;
    }

    public OfficeEntity() {
        // for serialization
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Array<LevelEntity> getLevelEntities() {
        return levelEntities;
    }

    public void setLevelEntities(Array<LevelEntity> levelEntities) {
        this.levelEntities = levelEntities;
    }

    public Array<ResidentEntity> getResidentEntities() {
        return residentEntities;
    }

    public void setResidentEntities(Array<ResidentEntity> residentEntities) {
        this.residentEntities = residentEntities;
    }

}
