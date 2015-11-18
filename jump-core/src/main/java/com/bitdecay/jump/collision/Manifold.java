package com.bitdecay.jump.collision;

import com.bitdecay.jump.geom.BitPoint;

/**
 * Created by Monday on 11/16/2015.
 */
public class Manifold {
    public final BitPoint axis;
    public final float distance;
    public final BitPoint result;

    public Manifold(BitPoint axis, float distance) {
        this.axis = axis;
        this.distance = distance;
        this.result = axis.scale(distance);
    }
}
