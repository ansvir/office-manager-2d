package com.tohant.om2d.actor;

public class SecurityRoom extends Room {

    private static final Room.Type TYPE = Type.SECURITY;

    public SecurityRoom(String id, float price, float cost, float x, float y, float width, float height) {
        super(id, price, cost, x, y, width, height);
    }

    public SecurityRoom(float price, float cost, float x, float y, float width, float height) {
        super(price, cost, x, y, width, height);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
