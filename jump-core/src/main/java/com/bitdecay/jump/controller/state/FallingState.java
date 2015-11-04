package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.geom.BitPoint;

/**
 * Created by Monday on 11/3/2015.
 */
public class FallingState extends SidewaysState {
    public FallingState() {
        System.out.println("Falling");
    }

    @Override
    public MotionState update(float delta, BitBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);
        if (body.grounded) {
            return new GroundedState();
        } else {
            return this;
        }
    }
}
