package com.bitdecay.jump.gdx.input;

/**
 * Created by MondayHopscotch on 7/20/2016.
 *
 * Interface to allow a binary answer to if a control
 * interaction is pressed
 */
public interface InputStateReporter {
    boolean isJustPressed();
    boolean isPressed();
}
