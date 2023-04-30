package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;

public class HallRoom extends Room {

    private final Type TYPE = Type.HALL;
    private float price;

    public HallRoom(String id, float price, float x, float y, float width, float height) {
        super(id, price);
        setPosition(x, y);
        setSize(width, height);
    }

    public HallRoom(float price, float x, float y, float width, float height) {
        super(price);
        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
