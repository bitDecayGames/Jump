package com.bitdecay.jump.control.state;

import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.control.ControlMap;

/**
 * Created by Monday on 11/3/2015.
 */
public interface JumperBodyControlState {
    void stateEntered(JumperBody body, ControlMap controls);
    JumperBodyControlState update(float delta, JumperBody body, ControlMap controls);
}
