package com.tohant.om2d.actor.man;

import static com.tohant.om2d.actor.man.Staff.Type.CLEANING;

public class CleaningStaff extends Staff {

    private static final Type TYPE = CLEANING;

    public CleaningStaff(float salary) {
        super(salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
