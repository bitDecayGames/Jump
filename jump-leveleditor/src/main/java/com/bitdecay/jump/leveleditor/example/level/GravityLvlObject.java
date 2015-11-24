package com.bitdecay.jump.leveleditor.example.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.UserSizedLevelObject;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

/**
 * Created by Monday on 11/22/2015.
 */
public class GravityLvlObject extends RenderableLevelObject implements UserSizedLevelObject {
    private final TextureRegion texture;

    public GravityLvlObject() {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/question.png")));
    }

    @Override
    public BitBody buildBody() {
        BitBody body = new BitBody();
        body.bodyType = BodyType.STATIC;
        body.aabb = new BitRectangle(rect);
        body.props.collides = false;
        return body;
    }

    @Override
    public String name() {
        return "Gravity Field";
    }

    @Override
    public TextureRegion texture() {
        return texture;
    }
}
