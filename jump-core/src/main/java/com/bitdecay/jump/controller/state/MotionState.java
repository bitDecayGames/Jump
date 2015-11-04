package com.bitdecay.jump.controller.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.controller.ControlMap;

/**
 * Created by Monday on 11/3/2015.
 */
public interface MotionState {
    MotionState update(float delta, BitBody body, ControlMap controls);
}
