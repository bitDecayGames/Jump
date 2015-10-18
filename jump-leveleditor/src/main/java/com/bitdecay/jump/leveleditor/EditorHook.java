package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bitdecay.jump.level.builder.LevelBuilderListener;

/**
 * Created by Monday on 10/18/2015.
 */
public interface EditorHook extends LevelBuilderListener {
    void update(float delta);
    void render(OrthographicCamera cam);
}
