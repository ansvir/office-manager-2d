package com.tohant.om2d.actor.man;

public class WorkerStaff extends Staff {

    private static final Type TYPE = Type.WORKER;

    public WorkerStaff(String id) {
        super(id, 0.0f);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
