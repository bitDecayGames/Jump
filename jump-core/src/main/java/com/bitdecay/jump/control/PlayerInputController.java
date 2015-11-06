package com.bitdecay.jump.control;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.state.GroundedControlState;
import com.bitdecay.jump.control.state.JumperBodyControlState;

public class PlayerInputController implements BitBodyController {
    private ControlMap controls;
    private JumperBodyControlState state = new GroundedControlState();

    public PlayerInputController(ControlMap controls) {
        this.controls = controls;
    }

    public void update(float delta, BitBody body) {
        if (body instanceof JumperBody) {
            JumperBodyControlState newState = state.update(delta, (JumperBody) body, controls);
            if (newState != state) {
                newState.stateEntered((JumperBody) body, controls);
            }
            state = newState;
        }
    }
}
