package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;

public interface MouseMode {
    void mouseDown(BitPointInt point, MouseButton button);

    /**
     * mouse moved while button is down
     *
     * @param point
     */
    void mouseDragged(BitPointInt point);

    void mouseUp(BitPointInt point, MouseButton button);

    /**
     * Mouse moved without button down
     *
     * @param point
     */
    void mouseMoved(BitPointInt point);

    void render(ShapeRenderer shaper, SpriteBatch spriteBatch);
}
