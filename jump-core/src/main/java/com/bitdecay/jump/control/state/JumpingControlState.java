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
        System.out.println("Jumping");
        firstUpdate = true;
    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {

        handleLeftRight(delta, body, controls, body.props.airAcceleration, body.props.airDeceleration);
        JumperProperties props;
        if (body.props instanceof JumperProperties) {
            props = (JumperProperties) body.props;
        } else {
            return this;
        }
        if (firstUpdate || !body.grounded) {
            firstUpdate = false;
            if (controls.isPressed(PlayerAction.JUMP) && jumpVariableHeightWindow <= props.jumpVariableHeightWindow) {
                if (body.jumpsRemaining == ((JumperProperties) body.props).jumpCount) {
                    // first jump
                    body.velocity.y = props.jumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, props.jumpStrength) ? -1 : 1);
                } else {
                    body.velocity.y = props.jumpDoubleJumpStrength * (MathUtils.sameSign(BitWorld.gravity.y, props.jumpDoubleJumpStrength) ? -1 : 1);
                }
                jumpVariableHeightWindow += delta;
                return this;
            } else {
                return new FallingControlState();
            }
        } else {
            return new GroundedControlState();
        }
    }
}
