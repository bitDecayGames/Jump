package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Created by MondayHopscotch on 7/22/2016.
 */
public class ControllerPOVState extends ControllerAdapter implements InputStateReporter {

    private Controller controller;
    private PovDirection povDirection;

    private boolean buttonDown = false;
    private boolean fresh = false;

    public ControllerPOVState(Controller controller, PovDirection povDirection) {
        this.controller = controller;
        this.povDirection = povDirection;
        controller.addListener(this);
    }

    @Override
    public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
        if (this.controller == controller) {
            if (this.povDirection == value) {
                buttonDown = true;
                fresh = true;
            } else {
                buttonDown = false;
                fresh = false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isJustPressed() {
        if (buttonDown && fresh) {
            fresh = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPressed() {
        fresh = false;
        return buttonDown;
    }
}
