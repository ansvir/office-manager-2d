package com.tohant.om2d.actor.man;

public class CaffeStaff extends Staff {

    private final Type TYPE = Type.CAFFE;

    public CaffeStaff(float salary) {
        super(salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
