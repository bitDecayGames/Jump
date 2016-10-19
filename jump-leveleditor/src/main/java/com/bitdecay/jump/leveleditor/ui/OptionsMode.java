package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.leveleditor.ui.menus.MenuPage;
import com.bitdecay.jump.leveleditor.utils.EditorKeys;

public enum OptionsMode {
    /*
     * Can't add key bindings to save/load because they pop up a dialog which causes the game to lose focus.
     * This causes the key up event is lost and the app thinks the buttons are pressed when they aren't
     */
    SAVE_LEVEL("Save", ModeType.ACTION, null, null, "icons/save.png"),
    LOAD_LEVEL("Load", ModeType.ACTION, null, null, "icons/load.png"),
    REFRESH("Refresh", ModeType.ACTION, null, EditorKeys.REFRESH, "icons/refresh.png"),
    UNDO("Undo", ModeType.ACTION, null, EditorKeys.UNDO, "icons/undo.png"),
    REDO("Redo", ModeType.ACTION, null, EditorKeys.REDO, "icons/redo.png"),

    // Modes that are toggle
    RENDER("Render", ModeType.MOUSE, MenuPage.RenderMenu, null, "icons/render.png"),
    SELECT("Select", ModeType.MOUSE, null, null, "icons/select.png"),
    MOVING_PLATFORM("Moving Plat", ModeType.MOUSE, null, null, "icons/movingPlatform.png"),
    CREATE("Create", ModeType.MOUSE, MenuPage.TileMenu, null, "icons/tiles.png"),
    SINGLE("Single", ModeType.MOUSE, MenuPage.TileMenu, null, "icons/tiles.png"),
    ONEWAY("One Way", ModeType.MOUSE, MenuPage.TileMenu, null, "icons/oneway.png"),
    THEME("Theme", ModeType.MOUSE, MenuPage.ThemeMenu, null, "icons/theme.png"),
    LAYERS("Layers", ModeType.MOUSE, MenuPage.LayersMenu, null, "icons/layers.png"),

    // Modes that are a one-time action
    DROP_OBJECT("Object", ModeType.MOUSE, MenuPage.LevelObjectMenu, null, "icons/object.png"),
    DROP_SIZABLE_OBJECT("Object", ModeType.MOUSE, MenuPage.LevelObjectMenu, null, null), // hidden differentiator for dropping objects
    TRIGGERS("Triggers", ModeType.MOUSE, null, null, "icons/trigger.png"),
    SET_SPAWN("Test Spawn", ModeType.MOUSE, null, null, "icons/debugSpawn.png"),
    PROPERTY_INSPECT("Inspect", ModeType.MOUSE, null, null, "icons/inspect.png"),
    DELETE("Delete", ModeType.MOUSE, null, null, "icons/delete.png");



    public final String label;
    public final ModeType type;
    public final MenuPage menu;
    public final EditorKeys hotkey;
    public final String icon;

    OptionsMode(String label, ModeType type, MenuPage menu, EditorKeys hotkey, String icon) {
        this.label = label;
        this.type = type;
        this.menu = menu;
        this.hotkey = hotkey;
        this.icon = icon;
    }
}
