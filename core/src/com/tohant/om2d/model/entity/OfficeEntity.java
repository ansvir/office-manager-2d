package com.tohant.om2d.model.entity;

import com.badlogic.gdx.utils.Array;

import java.io.Serializable;

public class OfficeEntity implements Serializable {

    private String id;
    private String name;
    private float popularity;
    private float budget;
    private Array<String> levelsIds;

    public OfficeEntity(String id, String name, float popularity, float budget, Array<String> levelsIds) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.budget = budget;
        this.levelsIds = levelsIds;
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

    public Array<String> getLevelsIds() {
        return levelsIds;
    }

    public void setLevelsIds(Array<String> levelsIds) {
        this.levelsIds = levelsIds;
    }

}
