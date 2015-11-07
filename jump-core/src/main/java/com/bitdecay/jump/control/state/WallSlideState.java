package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.geom.MathUtils;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/6/2015.
 */
public class WallSlideState implements JumperBodyControlState {
    // We need to push the body into the wall constantly so we know the wall is there
    static int force_into_wall = 60;

    boolean wallLeft;
    int directionOfWall;

    JumperProperties props;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        wallLeft =  body.lastResolution.x > 0;
        directionOfWall = wallLeft ? -force_into_wall : force_into_wall;
        body.velocity.x = directionOfWall;
        props = (JumperProperties) body.props;
    }

    @Override
    public void stateExited(JumperBody body, ControlMap controls) {

    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {
        if (body.grounded) {
            return new GroundedControlState();
        } else {

            if (!MathUtils.opposing(directionOfWall, body.lastResolution.x)) {
                body.velocity.x = 0;
                return new FallingControlState();
            }

            if (props.wallJumpEnabled && controls.isJustPressed(PlayerAction.JUMP)) {
                body.velocity.x = props.wallJumpLaunchPower * (wallLeft ? 1 : -1);
                return new JumpingControlState();
            }

            if (controls.isPressed(wallLeft ? PlayerAction.RIGHT : PlayerAction.LEFT)) {
                return new FallingControlState();
            }
            body.velocity.x = directionOfWall;
            return this;
        }
    }
}