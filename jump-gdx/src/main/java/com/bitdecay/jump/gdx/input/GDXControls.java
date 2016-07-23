package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.Input.Keys;
import com.bitdecay.jump.control.ControlMap;
import com.bitdecay.jump.control.PlayerAction;

import java.util.HashMap;
import java.util.Map;

public class GDXControls implements ControlMap {
    public static final GDXControls defaultMapping = new GDXControls();

    static {
        defaultMapping.set(PlayerAction.LEFT, new KeyState(Keys.A));
        defaultMapping.set(PlayerAction.RIGHT, new KeyState(Keys.D));
        defaultMapping.set(PlayerAction.UP, new KeyState(Keys.W));
        defaultMapping.set(PlayerAction.DOWN, new KeyState(Keys.S));
        defaultMapping.set(PlayerAction.JUMP, new KeyState(Keys.SPACE));
    }

    Map<PlayerAction, InputStateReporter> map = new HashMap<>();

    public InputStateReporter get(PlayerAction btn) {
        return map.get(btn);
    }

    public void set(PlayerAction btn, InputStateReporter input) {
        map.put(btn, input);
    }

    @Override
    public boolean isJustPressed(PlayerAction action) {
        if (map.containsKey(action)) {
            return map.get(action).isJustPressed();
        } else {
            return false;
        }
    }

    @Override
    public boolean isPressed(PlayerAction action) {
        if (map.containsKey(action)) {
            return map.get(action).isPressed();
        } else {
            return false;
        }
    }
}
