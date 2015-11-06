package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.controller.PlayerAction;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class GroundedState extends SidewaysState {
    public float jumpGracePeriod = 0;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        body.jumpsPerformed = 0;
        body.jumpsRemaining = ((JumperProperties)body.props).jumpCount;
        System.out.println("Grounded");
    }

    @Override
    public JumperControlState update(float delta, JumperBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.acceleration, body.props.deceleration);
        return checkStateChange(delta, body, controls);
    }

    private JumperControlState checkStateChange(float delta, JumperBody body, ControlMap controls) {
        JumperProperties props;
        if (body.props instanceof JumperProperties) {
            props = (JumperProperties) body.props;
        } else {
            return this;
        }
        if (!body.grounded) {
            jumpGracePeriod += delta;
            if (jumpGracePeriod > props.jumpGraceWindow) {
                return new FallingState();
            }
        }
        if (controls.isJustPressed(PlayerAction.JUMP)) {
            return new JumpingState();
        }
        return this;
    }
}
