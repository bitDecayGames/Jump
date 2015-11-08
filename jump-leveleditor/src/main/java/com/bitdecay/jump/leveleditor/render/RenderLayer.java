package com.bitdecay.jump.leveleditor.render;

/**
 * Created by Monday on 11/8/2015.
 */
public enum RenderLayer {
    MOUSE_TOOLS(false),
    UI_STRINGS(true),
    GAME(true),
    DYNAMIC_BODIES(true),
    KINETIC_BODIES(true),
    STATIC_BODIES(true),
    TILES(true),
    SPEED(true),
    STATE_HELPERS(true),
    LEVEL_GRID(true);

    public boolean userEditable;
    public boolean enabled = true;

    RenderLayer(boolean userEditable) {
        this.userEditable = userEditable;
    }
}
