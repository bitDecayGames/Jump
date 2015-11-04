package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.controller.PlayerAction;
import com.bitdecay.jump.geom.MathUtils;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class JumpingState extends SidewaysState {
    float jumpVariableHeightWindow = 0;

    public JumpingState() {
        System.out.println("Jumping");
    }

    @Override
    public MotionState update(float delta, BitBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);
        JumperProperties props;
        if (body.props instanceof JumperProperties) {
            props = (JumperProperties) body.props;
        } else {
            return this;
        }
        if (jumpVariableHeightWindow == 0) {
            // first update of jump, just jump
            body.velocity.y = props.jumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, props.jumpStrength) ? -1 : 1);
            jumpVariableHeightWindow += delta;
            return this;
        } else if (body.grounded) {
            // if we are grounded by the engine at any point, we are now grounded
            return new GroundedState();
        }

        if (controls.isPressed(PlayerAction.JUMP) && jumpVariableHeightWindow <= props.jumpVariableHeightWindow) {
            body.velocity.y = props.jumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, props.jumpStrength) ? -1 : 1);
            jumpVariableHeightWindow += delta;
        } else {
            return new FallingState();
        }
        return this;
    }
}
