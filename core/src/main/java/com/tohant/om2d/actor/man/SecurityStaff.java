package com.tohant.om2d.actor.man;

public class SecurityStaff extends Staff {

    private static final Staff.Type TYPE = Type.SECURITY;

    public SecurityStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
