package com.tohant.om2d.model.entity;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public abstract class AbstractActorEntity implements Serializable {

    @DatabaseField
    private String actorName;

    public AbstractActorEntity(String actorName) {
        this.actorName = actorName;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

}
