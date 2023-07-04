package com.tohant.om2d.actor.man;

public class CaffeStaff extends Staff {

    private final Type TYPE = Type.CAFFE;

    public CaffeStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
