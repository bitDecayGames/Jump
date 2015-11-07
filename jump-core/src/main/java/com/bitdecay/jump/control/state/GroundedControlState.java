package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 11/3/2015.
 */
public class GroundedControlState extends SidewaysControlState {
    public float jumpGracePeriod = 0;

    @Override
    public void stateEntered(JumperBody body, ControlMap controls) {
        body.jumpsPerformed = 0;
        body.jumpsRemaining = ((JumperProperties)body.props).jumpCount;
        System.out.println("Grounded");
    }

    @Override
    public void stateExited(JumperBody body, ControlMap controls) {

    }

    @Override
    public JumperBodyControlState update(float delta, JumperBody body, ControlMap controls) {
        handleLeftRight(delta, body, controls, body.props.acceleration, body.props.deceleration);
        return checkStateChange(delta, body, controls);
    }

    private JumperBodyControlState checkStateChange(float delta, JumperBody body, ControlMap controls) {
        JumperProperties props;
        if (body.props instanceof JumperProperties) {
            props = (JumperProperties) body.props;
        } else {
            return this;
        }
        if (!body.grounded) {
            jumpGracePeriod += delta;
            if (jumpGracePeriod > props.jumpGraceWindow) {
                body.jumpsRemaining--;
                return new FallingControlState();
            }
        }
        if (body.jumpsRemaining > 0 && controls.isJustPressed(PlayerAction.JUMP) || (controls.isPressed(PlayerAction.JUMP) && body.bunnyHop)) {
            body.bunnyHop = false;
            return new JumpingControlState();
        }
        return this;
    }
}
