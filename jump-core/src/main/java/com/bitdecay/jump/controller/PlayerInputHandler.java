package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.controller.state.GroundedState;
import com.bitdecay.jump.controller.state.MotionState;

public class PlayerInputHandler {
    private BitBody body;
    private ControlMap controls;
    private JumperController bodyController;

    private MotionState state = new GroundedState();

    public void setBody(BitBody body, ControlMap controls) {
        this.body = body;
        this.controls = controls;
        bodyController = new JumperController();
        body.controller = bodyController;
    }

    public void update(float delta) {
        if (body != null) {
            state = state.update(delta, body, controls);
        }
    }
}
