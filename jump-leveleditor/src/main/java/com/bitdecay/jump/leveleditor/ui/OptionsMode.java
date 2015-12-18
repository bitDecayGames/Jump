package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.leveleditor.ui.menus.MenuPage;

public enum OptionsMode {
    SAVE_LEVEL("Save", ModeType.ACTION, null, "icons/save.png"),
    LOAD_LEVEL("Load", ModeType.ACTION, null, "icons/load.png"),
    REFRESH("Refresh", ModeType.ACTION, null, "icons/refresh.png"),
    UNDO("Undo", ModeType.ACTION, null, "icons/undo.png"),
    REDO("Redo", ModeType.ACTION, null, "icons/redo.png"),

    // Modes that are toggle
    RENDER("Layers", ModeType.MOUSE, MenuPage.RenderMenu, "icons/render.png"),
    SELECT("Select", ModeType.MOUSE, null, "icons/select.png"),
    MOVING_PLATFORM("Moving Plat", ModeType.MOUSE, null, "icons/movingPlatform.png"),
    CREATE("Create", ModeType.MOUSE, MenuPage.TileMenu, "icons/tiles.png"),
    ONEWAY("One Way", ModeType.MOUSE, MenuPage.TileMenu, "icons/oneway.png"),
    //erik
    FOREGROUND("Foreground", ModeType.MOUSE, MenuPage.TileMenu, "icons/oneway.png"),
    //end.erik
    THEME("Theme", ModeType.MOUSE, MenuPage.ThemeMenu, "icons/theme.png"),

    // Modes that are a one-time action
    DROP_OBJECT("Object", ModeType.MOUSE, MenuPage.LevelObjectMenu, "icons/object.png"),
    DROP_SIZABLE_OBJECT("Object", ModeType.MOUSE, MenuPage.LevelObjectMenu, null), // hidden differentiator for dropping objects
    TRIGGERS("Triggers", ModeType.MOUSE, null, "icons/trigger.png"),
    SET_SPAWN("Test Spawn", ModeType.MOUSE, null, "icons/debugSpawn.png"),
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
