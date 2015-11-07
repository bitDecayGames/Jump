package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.ControlMap;

/**
 * Created by Monday on 11/6/2015.
 */
public class WallSlideState implements JumperBodyControlState {
    boolean wallLeft;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        System.out.println("WALL SLIDE");
        wallLeft = body.lastResolution.x > 0;
    }

    @Override
    public void stateExited(JumperBody body, ControlMap controls) {

    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {
        if (body.grounded) {
            return new GroundedControlState();
        } else {
            if (wallLeft) {
                if (!(body.lastResolution.x > 0)) {
                    return new FallingControlState();
                }
                body.velocity.x = -1;
            } else {
                if (!(body.lastResolution.x < 0)) {
                    return new FallingControlState();
                }
                body.velocity.x = 1;
            }
            return this;
        }
    }
}
