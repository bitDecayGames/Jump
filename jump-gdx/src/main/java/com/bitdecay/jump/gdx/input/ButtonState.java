package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.Gdx;

/**
 * Created by Monday on 11/3/2015.
 */
public class ButtonState {
    int key;

    public ButtonState(int key) {
        this.key = key;
    }

    public boolean isJustPressed() {
        return Gdx.input.isKeyJustPressed(key);
    }

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(key);
    }
}
