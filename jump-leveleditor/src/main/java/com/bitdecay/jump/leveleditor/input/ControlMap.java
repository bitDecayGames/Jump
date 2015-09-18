package com.bitdecay.jump.leveleditor.input;

import com.badlogic.gdx.Input.Keys;

import java.util.HashMap;
import java.util.Map;

public class ControlMap {
    public static final ControlMap defaultMapping = new ControlMap();

    static {
        defaultMapping.set(PlayerAction.LEFT, Keys.A);
        defaultMapping.set(PlayerAction.RIGHT, Keys.D);
        defaultMapping.set(PlayerAction.UP, Keys.W);
        defaultMapping.set(PlayerAction.DOWN, Keys.S);
        defaultMapping.set(PlayerAction.JUMP, Keys.SPACE);
    }

    Map<PlayerAction, Integer> map = new HashMap<>();

    public int get(PlayerAction btn) {
        return map.get(btn);
    }

    public void set(PlayerAction btn, int key) {
        map.put(btn, key);
    }
}
