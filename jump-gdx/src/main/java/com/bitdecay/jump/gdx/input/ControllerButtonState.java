package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.controllers.Controller;

/**
 * Created by MondayHopscotch on 7/20/2016.
 */
public class ControllerButtonState implements InputStateReporter {

    private Controller controller;
    private int button;


    public ControllerButtonState(Controller controller, int button) {
        this.controller = controller;
        this.button = button;
    }

    @Override
    public boolean isJustPressed() {
        return controller.getButton(button);
    }

    @Override
    public boolean isPressed() {
        return controller.getButton(button);
    }
}
