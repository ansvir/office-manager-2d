package com.tohant.om2d.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

public class ObjectCell extends Actor {

    private final boolean isObstacle;

    public ObjectCell(String id, float x, float y, float width, float height, boolean isObstacle) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        this.isObstacle = isObstacle;
        debug();
    }

    public boolean isObstacle() {
        return isObstacle;
    }

}
