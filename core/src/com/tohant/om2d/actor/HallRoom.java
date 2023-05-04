package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;

public class HallRoom extends Room {

    private final Type TYPE = Type.HALL;

    public HallRoom(String id, float price, float cost, float x, float y, float width, float height) {
        super(id, price, cost, x, y, width, height);
    }

    public HallRoom(float price, float cost, float x, float y, float width, float height) {
        super(price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getTexturePath() {
        return "hall.png";
    }

}
