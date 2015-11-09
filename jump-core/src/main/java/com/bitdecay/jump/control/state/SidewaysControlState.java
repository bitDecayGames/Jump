package com.bitdecay.jump.control.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;

/**
 * Created by Monday on 11/4/2015.
 */
public abstract class SidewaysControlState implements JumperBodyControlState {
    protected void handleLeftRight(float delta, BitBody body, ControlMap controls, int accel, int decel) {
        boolean requestLeft = controls.isPressed(PlayerAction.LEFT);
        boolean requestRight = controls.isPressed(PlayerAction.RIGHT);

        if (requestLeft || requestRight) {
            body.facing = requestLeft ? Facing.LEFT : Facing.RIGHT;
            if (requestLeft) {
                if (body.velocity.x > 0) {
                    if (decel == 0) {
                        body.velocity.x = 0;
                    }
                    body.velocity.x = accel == 0 ? -body.props.maxVoluntarySpeed : Math.max(-body.props.maxVoluntarySpeed, body.velocity.x
                            - (accel + decel) * delta);
                } else if (body.velocity.x < -body.props.maxVoluntarySpeed) {
                    body.velocity.x += (decel * delta);
                } else {
                    body.velocity.x = accel == 0 ? -body.props.maxVoluntarySpeed : Math.max(-body.props.maxVoluntarySpeed, body.velocity.x
                            - accel * delta);
                }
            } else {
                if (body.velocity.x < 0) {
                    if (decel == 0) {
                        body.velocity.x = 0;
                    }
                    body.velocity.x = accel == 0 ? body.props.maxVoluntarySpeed : Math.min(body.props.maxVoluntarySpeed, body.velocity.x
                            + (accel + decel) * delta);
                } else if (body.velocity.x > body.props.maxVoluntarySpeed) {
                    body.velocity.x -= (decel * delta);
                } else {
                    body.velocity.x = accel == 0 ? body.props.maxVoluntarySpeed : Math.min(body.props.maxVoluntarySpeed, body.velocity.x
                            + accel * delta);
                }
            }
        } else {
            if (decel == 0) {
                body.velocity.x = 0;
            } else {
                if (body.velocity.x > 0) {
                    body.velocity.x = Math.max(0, body.velocity.x - decel * delta);
                }
                if (body.velocity.x < 0) {
                    body.velocity.x = Math.min(0, body.velocity.x + decel * delta);
                }
            }
        }
    }
}
