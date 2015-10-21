package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.level.builder.LevelBuilderListener;
import com.bitdecay.jump.level.builder.LevelObject;

import java.util.List;

/**
 * Created by Monday on 10/18/2015.
 */
public interface EditorHook extends LevelBuilderListener {
    void update(float delta);
    void render(OrthographicCamera cam);
    BitWorld getWorld();
    List<RenderableLevelObject> getCustomObjects();
}
