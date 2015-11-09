package com.bitdecay.jump.geom;

import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.annotation.ValueRange;

/**
 * Created by Monday on 10/11/2015.
 */
public class PathPoint {
    @ValueRange(min = 0, max = 500)
    public int speed;
    @ValueRange(min = 0, max = 10)
    public float stayTime;

    @CantInspect
    public BitPoint destination;

    public PathPoint() {
        // Here for JSON
    }

    public PathPoint(BitPoint point, int speed, float stay) {
        destination = point;
        this.speed = speed;
        stayTime = stay;
    }
}
