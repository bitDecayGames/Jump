package com.bitdecay.jump.leveleditor.example.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.level.builder.LevelObject;

import java.util.List;

/**
 * Created by Monday on 10/20/2015.
 */
public abstract class GameObject {
    public abstract List<BitBody> build(LevelObject template);
    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);
}
