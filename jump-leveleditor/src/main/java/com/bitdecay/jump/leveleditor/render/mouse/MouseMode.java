package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;

public interface MouseMode {
    public void mouseDown(BitPointInt point, MouseButton button);

    /**
     * mouse moved while button is down
     *
     * @param point
     */
    public void mouseDragged(BitPointInt point);

    public void mouseUp(BitPointInt point, MouseButton button);

    /**
     * Mouse moved without button down
     *
     * @param point
     */
    public void mouseMoved(BitPointInt point);

    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch);
}
