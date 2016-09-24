package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitRectangle;

/**
 * Created by Monday on 9/19/2016.
 */
public class MockLevelObject extends LevelObject {
    public MockLevelObject(BitRectangle bitRectangle) {
        this.rect = bitRectangle;
    }

    @Override
    public BitBody buildBody() {
        return null;
    }

    @Override
    public String name() {
        return "Test Object";
    }
}
