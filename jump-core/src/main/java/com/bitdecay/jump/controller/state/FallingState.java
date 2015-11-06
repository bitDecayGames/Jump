package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.controller.PlayerAction;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class FallingState extends SidewaysState {
    float preJumpTimer;
    boolean waitingForRelease;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        waitingForRelease = controls.isPressed(PlayerAction.JUMP);
        body.jumpsRemaining--;
        preJumpTimer = 0;
        System.out.println("Falling");
    }

    @Override
    public JumperControlState update(float delta, JumperBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);
        if (!controls.isPressed(PlayerAction.JUMP)) {
            waitingForRelease = false;
        }

        if (body.grounded) {
            if (!waitingForRelease && controls.isPressed(PlayerAction.JUMP) && preJumpTimer <= ((JumperProperties)body.props).jumpGraceWindow) {
                return new JumpingState();
            } else {
                return new GroundedState();
            }
        } else if (!waitingForRelease && controls.isPressed(PlayerAction.JUMP)) {
            if (body.jumpsRemaining > 0) {
                return new JumpingState();
            } else {
                preJumpTimer += delta;
            }
        } else {
            preJumpTimer = 0;
        }
        return this;
    }
}
