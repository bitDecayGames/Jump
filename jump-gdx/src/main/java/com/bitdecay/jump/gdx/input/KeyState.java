package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.Gdx;

/**
 * Reads directly from Gdx.input to report back the state of a key
 *
 * Created by Monday on 11/3/2015.
 */
public class KeyState implements InputStateReporter {
    int key;

    public KeyState(int key) {
        this.key = key;
    }

    @Override
    public boolean isJustPressed() {
        return Gdx.input.isKeyJustPressed(key);
    }

    @Override
    public boolean isPressed() {
        return Gdx.input.isKeyPressed(key);
    }
}
