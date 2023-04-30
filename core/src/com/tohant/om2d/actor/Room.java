package com.tohant.om2d.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.UUID;

public abstract class Room extends Actor {

    private String id;

    public Room(String id) {
        this.id = id;
    }

    public Room() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum Type {
        HALL, OFFICE
    }

}
