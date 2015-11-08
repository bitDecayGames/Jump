package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.leveleditor.ui.menus.MenuPage;

public enum OptionsMode {
    SAVE_LEVEL("Save Level", ModeType.ACTION, null, "icons/save.png"),
    LOAD_LEVEL("Load Level", ModeType.ACTION, null, "icons/load.png"),
    UNDO("Undo", ModeType.ACTION, null, "icons/undo.png"),
    REDO("Redo", ModeType.ACTION, null, "icons/redo.png"),

    // Modes that are toggle
    SELECT("Select", ModeType.MOUSE, null, "icons/select.png"),
    MOVING_PLATFORM("Moving Platform", ModeType.MOUSE, null, "icons/movingPlatform.png"),
    CREATE("Create", ModeType.MOUSE, MenuPage.TileMenu, "icons/tiles.png"),
    ONEWAY("One Way", ModeType.MOUSE, MenuPage.TileMenu, "icons/oneway.png"),
    THEME("Set Theme", ModeType.MOUSE, MenuPage.ThemeMenu, "icons/theme.png"),

    // Modes that are a one-time action
    DROP_OBJECT("Drop Object", ModeType.MOUSE, MenuPage.LevelObjectMenu, "icons/object.png"),
    SET_SPAWN("Set Spawn Point", ModeType.MOUSE, null, "icons/debugSpawn.png"),
    PROPERTY_INSPECT("Inspect", ModeType.MOUSE, null, "icons/inspect.png"),
    DELETE("Delete", ModeType.MOUSE, null, "icons/delete.png");



    public final String label;
    public final ModeType type;
    public final String icon;
    public final MenuPage menu;

    OptionsMode(String label, ModeType type, MenuPage menu, String icon) {
        this.label = label;
        this.type = type;
        this.menu = menu;
        this.icon = icon;
    }
}
