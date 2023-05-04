package com.tohant.om2d.actor;

public class OfficeRoom extends Room {

    private final Type TYPE = Type.OFFICE;

    public OfficeRoom(String id, float price, float cost, float x, float y, float width, float height) {
        super(id, price, cost, x, y, width, height);
    }

    public OfficeRoom(float price, float cost, float x, float y, float width, float height) {
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
