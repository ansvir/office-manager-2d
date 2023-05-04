package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;

public class CleaningRoom extends Room {

    private static final Room.Type TYPE = Room.Type.CLEANING;

    public CleaningRoom(String id, float price, float cost, float x, float y, float width, float height) {
        super(id, price, cost, x, y, width, height);
    }

    public CleaningRoom(float price, float cost, float x, float y, float width, float height) {
        super(price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getTexturePath() {
        return null;
    }

}
