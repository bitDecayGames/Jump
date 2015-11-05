package com.bitdecay.jump.leveleditor.ui;

public enum OptionsMode {
    SELECT("Select", ModeType.MOUSE, 0),
    MOVING_PLATFORM("Moving Platform", ModeType.MOUSE, 0),
    DELETE("Delete", ModeType.MOUSE, 0),
    PROPERTY_INSPECT("Inspect", ModeType.MOUSE, 0),
    SET_SPAWN("Set Spawn Point", ModeType.MOUSE, 0),

    CREATE("Create", ModeType.MOUSE, 1),
    ONEWAY("One Way", ModeType.MOUSE, 1),

    DROP_OBJECT("Drop Object", ModeType.MOUSE, 2),

    THEME("Set Theme", ModeType.MOUSE, 4),

    SAVE_LEVEL("Save Level", ModeType.ACTION, -1),
    LOAD_LEVEL("Load Level", ModeType.ACTION, -1),
    UNDO("Undo", ModeType.ACTION, -1),
    REDO("Redo", ModeType.ACTION, -1);

    public final String label;
    public final ModeType type;
    public final int group;

    OptionsMode(String label, ModeType type, int group) {
        this.label = label;
        this.type = type;
        this.group = group;
    }
}
