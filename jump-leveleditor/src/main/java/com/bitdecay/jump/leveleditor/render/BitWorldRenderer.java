package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bitdecay.jump.collision.BitWorld;

public interface BitWorldRenderer {
    void render(BitWorld world, OrthographicCamera cam);
}
