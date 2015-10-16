package com.bitdecay.jump.leveleditor.ui;

import com.badlogic.gdx.Input;

public enum OptionsMode {
    SELECT("Select", ModeType.MOUSE, 0, Input.Keys.S),
    CREATE("Create", ModeType.MOUSE, 0, Input.Keys.C),
    ONEWAY("One Way", ModeType.MOUSE, 0, Input.Keys.O),
    MOVING_PLATFORM("Moving Platform", ModeType.MOUSE, 0, Input.Keys.M),
    STATIC("Static", ModeType.MOUSE, 0, Input.Keys.UNKNOWN),
    DELETE("Delete", ModeType.MOUSE, 0, Input.Keys.D),

    UNDO("Undo", ModeType.ACTION, 0, Input.Keys.Z),
    REDO("Redo", ModeType.ACTION, 0, Input.Keys.Y),

    SET_TEST_PLAYER("Set Test Player", ModeType.MOUSE, 1, -1),
    SAVE_PLAYER("Save Player Props", ModeType.ACTION, 1, -1),
    LOAD_PLAYER("Load Player Props", ModeType.ACTION, 1, -1),

    SET_SPAWN("Set Spawn Point", ModeType.MOUSE, 2, -1),
    SET_MAT_DIR("Set Material Directory", ModeType.ACTION, 2, -1),

    SAVE_LEVEL("Save Level", ModeType.ACTION, -1, -1),
    LOAD_LEVEL("Load Level", ModeType.ACTION, -1, -1);

    public final String label;
    public final ModeType type;
    public final int group;
    public final int hotkey;

    private OptionsMode(String label, ModeType type, int group, int hotkey) {
        this.label = label;
        this.type = type;
        this.group = group;
        this.hotkey = hotkey;
    }
}
