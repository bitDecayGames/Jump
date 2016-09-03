package com.bitdecay.jump.leveleditor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Monday on 9/3/2016.
 */
public enum EditorKeys {
    // Help
    HELP("Show Hotkey Help", Input.Keys.F1),

    // World Controls
    PAUSE("Pause/Resume", Input.Keys.GRAVE),
    STEP_WORLD("Step World", Input.Keys.PLUS, Input.Keys.EQUALS),
    ROLL_WORLD("Roll World", Input.Keys.MINUS),

    // Camera controls
    PAN_LEFT("Camera Left", Input.Keys.LEFT),
    PAN_RIGHT("Camera Right", Input.Keys.RIGHT),
    PAN_UP("Camera Up", Input.Keys.UP),
    PAN_DOWN("Camera Down", Input.Keys.DOWN),
    ZOOM_IN("Camera Zoom In", Input.Keys.NUM_2, Input.Keys.NUMPAD_2),
    ZOOM_OUT("Camera Zoom Out", Input.Keys.NUM_1, Input.Keys.NUMPAD_1),

    // Tool Controls
    DELETE_SELECTED("Deleted Selected Objects", Input.Keys.BACKSPACE, Input.Keys.FORWARD_DEL),
    DISABLE_SNAP("Disable Snap (Hold)", Input.Keys.SHIFT_LEFT);


    private final String name;
    private final int[] keys;
    private final String keyHelp;

    EditorKeys(String name, int... keyList) {
        this.name = name;
        this.keys = keyList;

        StringBuilder builder = new StringBuilder();

        for (int key : keys) {
            if (builder.length() > 0) {
                builder.append(" or ");
            }
            builder.append(Input.Keys.toString(key));
        }
        keyHelp = builder.toString();
    }

    public boolean isPressed() {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isJustPressed() {
        for (int key : keys) {
            if (Gdx.input.isKeyJustPressed(key)) {
                return true;
            }
        }
        return false;
    }

    public String getHelp() {

        return name + ": " + keyHelp;
    }
}
