package com.bitdecay.jump.leveleditor.example.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 9/28/2016.
 */
public class PropertyTestLevelObject extends RenderableLevelObject {

    @CantInspect
    TextureRegion texture;

    public String testString;

    public Map<String, String> map = new HashMap<>();

    public PropertyTestLevelObject() {
        rect = new BitRectangle(0, 0, 16 ,16);

        this.texture = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/question.png")));
        texture.setRegionHeight(16);
        texture.setRegionWidth(16);

        map.put("stringOne", "test");
        map.put("stringTwo", "otherTest");
        map.put("stringThree", "otherTest");
        map.put("stringFour", "otherTest");
        map.put("stringFive", "otherTest");
        map.put("stringSix", "otherTest");
        map.put("stringSeven", "otherTest");
    }

    @Override
    public BitBody buildBody() {
        return null;
    }

    @Override
    public String name() {
        return "PropertyTest";
    }

    @Override
    public TextureRegion texture() {
        return texture;
    }
}
