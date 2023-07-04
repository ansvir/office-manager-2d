package com.tohant.om2d.actor.man;

public class CleaningStaff extends Staff {

    private static final Type TYPE = Type.CLEANING;

    public CleaningStaff(String id, float salary) {
        super(id, salary);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

}
