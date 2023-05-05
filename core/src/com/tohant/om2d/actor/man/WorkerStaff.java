package com.tohant.om2d.actor.man;

import static com.tohant.om2d.actor.man.Staff.Type.WORKER;

public class WorkerStaff extends Staff {

    private static final Type TYPE = WORKER;

    public WorkerStaff() {
        super(0.0f);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
