package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.controller.state.GroundedState;
import com.bitdecay.jump.controller.state.JumperControlState;

public class PlayerInputController implements BitBodyController {
    private ControlMap controls;
    private JumperControlState state = new GroundedState();

    public PlayerInputController(ControlMap controls) {
        this.controls = controls;
    }

    public void update(float delta, BitBody body) {
        if (body instanceof JumperBody) {
            JumperControlState newState = state.update(delta, (JumperBody) body, controls);
            if (newState != state) {
                newState.stateEntered((JumperBody) body, controls);
            }
            state = newState;
        }
    }
}
