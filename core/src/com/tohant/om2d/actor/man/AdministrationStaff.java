package com.tohant.om2d.actor.man;

import static com.tohant.om2d.actor.man.Staff.Type.ADMINISTRATION;

public class AdministrationStaff extends Staff {

    private static final Type TYPE = ADMINISTRATION;

    public AdministrationStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
