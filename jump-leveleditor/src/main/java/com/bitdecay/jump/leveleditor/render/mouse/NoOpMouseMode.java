package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;

/**
 * Created by Monday on 11/8/2015.
 */
public class NoOpMouseMode implements MouseMode {
    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {

    }

    @Override
    public void mouseDragged(BitPointInt point) {

    }

    @Override
    public void mouseUp(BitPointInt point, MouseButton button) {

    }

    @Override
    public void mouseMoved(BitPointInt point) {

    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {

    }
}
