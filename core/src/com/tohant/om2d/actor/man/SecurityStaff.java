package com.tohant.om2d.actor.man;

import static com.tohant.om2d.actor.man.Staff.Type.SECURITY;

public class SecurityStaff extends Staff {

    private static final Staff.Type TYPE = SECURITY;

    public SecurityStaff(float salary) {
        super(salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
