package com.tohant.om2d.actor;

public interface ToggleActor {

    void toggle();

    default void forceToggle(boolean value) {}

}
