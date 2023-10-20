package com.tohant.om2d.actor;

/**
 * Interface that marks a class, usually UI actor that can be toggled (on or off, true or false, etc.)
 * Toggle value can be only 2-valued (i.e. it can be only boolean or any double valued type, parsed to boolean).
 */
public interface ToggleActor {

    /**
     * Toggle actor and change its behaviour, usually vice versa, e.g. visible and not visible.
     */
    void toggle();

    /**
     * Actor that implements that method can manually set toggle option of itself.
     * @param value Toggle value.
     */
    default void forceToggle(boolean value) {}

}
