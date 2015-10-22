package com.bitdecay.jump.leveleditor.example.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

/**
 * Created by Monday on 10/19/2015.
 */
// make this object specify a class that it is? does that make any sense?
public class SecretThing extends RenderableLevelObject {
    private TextureRegion texture;

    public SecretThing() {
        rect = new BitRectangle(0,0,100,100);
        this.texture = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/question.png")));
    }

    @Override
    public BitBody buildBody() {
        BitBody body = new BitBody();
        body.bodyType = BodyType.STATIC;
        body.aabb = new BitRectangle(rect);
        return body;
    }

    @Override
    public String name() {
        return "Secret Thing";
    }

    @Override
    public TextureRegion texture() {
        return texture;
    }
}