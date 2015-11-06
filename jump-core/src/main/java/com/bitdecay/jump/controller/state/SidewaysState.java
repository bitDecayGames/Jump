package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.controller.PlayerAction;
import com.bitdecay.jump.geom.BitPoint;

/**
 * Created by Monday on 11/4/2015.
 */
public abstract class SidewaysState implements JumperControlState {
    protected void handleLeftRight(float delta, BitBody body, ControlMap controls, BitPoint accel, BitPoint decel) {
        boolean requestLeft = controls.isPressed(PlayerAction.LEFT);
        boolean requestRight = controls.isPressed(PlayerAction.RIGHT);

        if (requestLeft || requestRight) {
            body.facing = requestLeft ? Facing.LEFT : Facing.RIGHT;
            if (requestLeft) {
                if (body.velocity.x > 0) {
                    if (decel.x == 0) {
                        body.velocity.x = 0;
                    }
                    body.velocity.x = accel.x == 0 ? -body.props.maxSpeed.x : Math.max(-body.props.maxSpeed.x, body.velocity.x
                            - (accel.x + decel.x) * delta);
                } else {
                    body.velocity.x = accel.x == 0 ? -body.props.maxSpeed.x : Math.max(-body.props.maxSpeed.x, body.velocity.x
                            - accel.x * delta);
                }
            } else {
                if (body.velocity.x > 0) {
                    body.velocity.x = accel.x == 0 ? body.props.maxSpeed.x : Math.min(body.props.maxSpeed.x, body.velocity.x
                            + accel.x * delta);
                } else {
                    if (decel.x == 0) {
                        body.velocity.x = 0;
                    }
                    body.velocity.x = accel.x == 0 ? body.props.maxSpeed.x : Math.min(body.props.maxSpeed.x, body.velocity.x
                            + (accel.x + decel.x) * delta);
                }
            }
        } else {
            if (decel.x == 0) {
                body.velocity.x = 0;
            } else {
                if (body.velocity.x > 0) {
                    body.velocity.x = Math.max(0, body.velocity.x - decel.x * delta);
                }
                if (body.velocity.x < 0) {
                    body.velocity.x = Math.min(0, body.velocity.x + decel.x * delta);
                }
            }
        }
    }
}
