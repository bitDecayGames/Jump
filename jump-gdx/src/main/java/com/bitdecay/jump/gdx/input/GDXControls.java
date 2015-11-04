package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.Input.Keys;
import com.bitdecay.jump.controller.ControlMap;
import com.bitdecay.jump.controller.PlayerAction;

import java.util.HashMap;
import java.util.Map;

public class GDXControls implements ControlMap {
    public static final GDXControls defaultMapping = new GDXControls();

    static {
        defaultMapping.set(PlayerAction.LEFT, Keys.A);
        defaultMapping.set(PlayerAction.RIGHT, Keys.D);
        defaultMapping.set(PlayerAction.UP, Keys.W);
        defaultMapping.set(PlayerAction.DOWN, Keys.S);
        defaultMapping.set(PlayerAction.JUMP, Keys.SPACE);
    }

    Map<PlayerAction, ButtonState> map = new HashMap<>();

    public ButtonState get(PlayerAction btn) {
        return map.get(btn);
    }

    public void set(PlayerAction btn, int key) {
        map.put(btn, new ButtonState(key));
    }

    @Override
    public boolean isJustPressed(PlayerAction action) {
        return map.get(action).isJustPressed();
    }

    @Override
    public boolean isPressed(PlayerAction action) {
        return map.get(action).isPressed();
    }
}
