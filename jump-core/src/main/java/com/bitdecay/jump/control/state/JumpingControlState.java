package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.geom.MathUtils;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class JumpingControlState extends SidewaysControlState {
    /**
     * A simple flag so we can make sure actually leaves the ground on the first update of a jump
     */
    boolean firstUpdate;
    float jumpVariableHeightWindow = 0;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        body.jumpsPerformed++;
        firstUpdate = true;
    }

    @Override
    public void stateExited(JumperBody body, ControlMap controls) {
        body.jumpsRemaining--;
    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {

        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);
        if (body.jumperProps.jumpHittingHeadStopsJump){
            if (BitWorld.gravity.dot(body.lastResolution) > 0) {
                return new FallingControlState();
            }
        }

        if (firstUpdate || !body.grounded) {
            firstUpdate = false;
            if (controls.isPressed(PlayerAction.JUMP) && jumpVariableHeightWindow <= body.jumperProps.jumpVariableHeightWindow) {
                if (body.jumpsRemaining == body.jumperProps.jumpCount) {
                    // first jump
                    int desiredJumpSpeed = body.jumperProps.jumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, body.jumperProps.jumpStrength) ? -1 : 1);
                    if (body.velocity.y <= desiredJumpSpeed) {
                        body.velocity.y = desiredJumpSpeed;
                    }
                } else {
                    int desiredJumpSpeed = body.jumperProps.jumpDoubleJumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, body.jumperProps.jumpDoubleJumpStrength) ? -1 : 1);
                    if (body.velocity.y <= desiredJumpSpeed) {
                        body.velocity.y = desiredJumpSpeed;
                    }
                }
                jumpVariableHeightWindow += delta;
                return this;
            } else {
                return new FallingControlState();
            }
        } else {
            if (body.jumperProps.wallSlideEnabled && BitWorld.gravity.dot(body.currentAttempt) > 0 && body.lastResolution.x != 0) {
                return new WallSlideState();
            }
            return new GroundedControlState();
        }
    }
}
