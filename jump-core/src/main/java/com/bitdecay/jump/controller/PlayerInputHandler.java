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
//            // make sure we only send one jump request through per push of the button.
//            if (controls.isJustPressed(PlayerAction.JUMP)) {
//                bodyController.startJump();
//            } else if (!controls.isPressed(PlayerAction.JUMP)) {
//                bodyController.stopJump();
//            }
//            if (controls.isPressed(PlayerAction.LEFT)) {
//                bodyController.goLeft(true);
//            } else {
//                bodyController.goLeft(false);
//            }
//            if (controls.isPressed(PlayerAction.RIGHT)) {
//                bodyController.goRight(true);
//            } else {
//                bodyController.goRight(false);
//            }
        }
    }
}
