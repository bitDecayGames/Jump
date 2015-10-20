package com.bitdecay.jump.leveleditor.example;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.builder.LevelObject;

/**
 * Created by Monday on 10/19/2015.
 */
// make this object specify a class that it is? does that make any sense?
public class SecretThing extends LevelObject {

    public SecretThing() {
        rect = new BitRectangle(0,0,100,100);
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
}