package com.tohant.om2d.actor;

public class OfficeRoom extends Room {

    private final Type TYPE = Type.OFFICE;

    public OfficeRoom(String id, float price, float x, float y, float width, float height) {
        super(id, price);
        setPosition(x, y);
        setSize(width, height);
    }

    public OfficeRoom(float price, float x, float y, float width, float height) {
        super(price);
        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
