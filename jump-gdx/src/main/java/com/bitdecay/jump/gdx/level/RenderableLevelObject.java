package com.bitdecay.jump.gdx.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.level.LevelObject;

/**
 * Created by Monday on 10/20/2015.
 */
public abstract class RenderableLevelObject extends LevelObject {
    public abstract TextureRegion texture();

    public RenderableLevelObject newInstance(){
        try {
            return this.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }
}
