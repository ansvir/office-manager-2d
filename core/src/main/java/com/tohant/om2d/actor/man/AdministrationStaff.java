package com.tohant.om2d.actor.man;

public class AdministrationStaff extends Staff {

    private static final Type TYPE = Type.ADMINISTRATION;

    public AdministrationStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
