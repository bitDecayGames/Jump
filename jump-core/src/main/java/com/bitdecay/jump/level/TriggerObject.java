package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;

/**
 * Created by Monday on 11/23/2015.
 */
public class TriggerObject extends LevelObject {
    public LevelObject triggerer;
    public LevelObject triggeree;

    public TriggerObject() {
        // Here for JSON
    }

    public TriggerObject(LevelObject triggerer, LevelObject triggeree) {
        super(new BitRectangle(triggerer.rect.center(), triggeree.rect.center()));
        this.triggerer = triggerer;
        this.triggeree = triggeree;
    }

    @Override
    public BitBody buildBody() {
        return null;
    }

    @Override
    public String name() {
        return "Trigger";
    }

    @Override
    public boolean selects(BitPointInt point) {
        return rect.xy.minus(point).len() < 10;
    }

    @Override
    public boolean selects(BitRectangle selectionRect) {
        return selectionRect.contains(rect.xy);
    }
}
