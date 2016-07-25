package com.bitdecay.jump.control;

/**
 * Created by Monday on 11/3/2015.
 */
public interface ControlMap {
    void enable();
    void disable();
    boolean isEnabled();
    boolean isJustPressed(PlayerAction action);
    boolean isPressed(PlayerAction action);
}
