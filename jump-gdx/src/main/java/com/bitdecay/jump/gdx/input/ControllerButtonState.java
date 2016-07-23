package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

/**
 * Created by MondayHopscotch on 7/20/2016.
 */
public class ControllerButtonState extends ControllerAdapter implements InputStateReporter {

    private Controller controller;
    private int button;

    private boolean buttonDown = false;
    private boolean fresh = false;


    public ControllerButtonState(Controller controller, int button) {
        this.controller = controller;
        this.button = button;
        controller.addListener(this);
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (this.controller == controller && this.button == buttonCode) {
            buttonDown = true;
            fresh = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonIndex) {
        if (this.controller == controller && this.button == buttonIndex) {
            buttonDown = false;
            fresh = false;
            return true;
        } else {
            return false;
        }
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
