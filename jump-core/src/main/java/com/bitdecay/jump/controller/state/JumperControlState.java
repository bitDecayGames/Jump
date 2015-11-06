package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.controller.ControlMap;

/**
 * Created by Monday on 11/3/2015.
 */
public interface JumperControlState {
    void stateEntered(JumperBody body, ControlMap controls);
    JumperControlState update(float delta, JumperBody body, ControlMap controls);
}
