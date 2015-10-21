package com.bitdecay.jump.gdx.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.level.builder.LevelObject;

/**
 * Created by Monday on 10/20/2015.
 */
public abstract class RenderableLevelObject extends LevelObject {
    public abstract TextureRegion texture();
}
