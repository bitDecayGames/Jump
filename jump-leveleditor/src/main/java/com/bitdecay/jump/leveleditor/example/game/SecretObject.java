package com.bitdecay.jump.leveleditor.example.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 10/20/2015.
 */
public class SecretObject extends GameObject {
    BitBody body;
    private TextureRegion texture;

    @Override
    public List<BitBody> build(LevelObject template) {
        body = template.buildBody();
        body.userObject = this;
        this.texture = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/question.png")));
        return Arrays.asList(body);
    }

    @Override
    public void update(float delta) {
        //no-op
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, body.aabb.xy.x, body.aabb.xy.y, body.aabb.width, body.aabb.height);
    }
}
