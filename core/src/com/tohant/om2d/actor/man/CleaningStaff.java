package com.tohant.om2d.actor.man;

import static com.tohant.om2d.actor.man.Staff.Type.CLEANING;

public class CleaningStaff extends Staff {

    private static final Type TYPE = CLEANING;

    public CleaningStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
