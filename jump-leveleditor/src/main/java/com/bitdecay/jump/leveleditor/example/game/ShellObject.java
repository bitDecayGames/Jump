package com.bitdecay.jump.leveleditor.example.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.level.builder.LevelObject;

/**
 * Created by Monday on 11/12/2015.
 */
public class ShellObject extends GameObject {

    @Override
    public BitBody build(LevelObject template) {
        BitBody body = template.buildBody();
        body.controller = new ShellController(250);
        return body;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
