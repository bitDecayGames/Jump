package com.bitdecay.jump.leveleditor.tools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A basic interface to define things a tool action must have
 */
public interface ToolAction {
    public TextureRegion getIcon();

    public String getToolTip();
}
