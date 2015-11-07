package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class FallingControlState extends SidewaysControlState {
    float preJumpTimer;
    boolean waitingForRelease;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        waitingForRelease = controls.isPressed(PlayerAction.JUMP);
        preJumpTimer = 0;
        System.out.println("Falling");
    }

    @Override
    public void stateExited(JumperBody body, ControlMap controls) {

    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);

        if (BitWorld.gravity.dot(body.currentAttempt) > 0 && body.lastResolution.x != 0) {
            return new WallSlideState();
        }

        if (!controls.isPressed(PlayerAction.JUMP)) {
            waitingForRelease = false;
        }

        if (body.grounded) {
            if (!waitingForRelease && controls.isPressed(PlayerAction.JUMP) && preJumpTimer <= ((JumperProperties)body.props).jumpGraceWindow) {
                body.bunnyHop = true;
            }
            return new GroundedControlState();
        } else if (!waitingForRelease && controls.isPressed(PlayerAction.JUMP)) {
            if (body.jumpsRemaining > 0) {
                return new JumpingControlState();
            } else {
                preJumpTimer += delta;
            }
        } else {
            preJumpTimer = 0;
        }
        return this;
    }
}
