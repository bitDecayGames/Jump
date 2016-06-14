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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathPoint pathPoint = (PathPoint) o;

        if (speed != pathPoint.speed) return false;
        if (Float.compare(pathPoint.stayTime, stayTime) != 0) return false;
        return !(destination != null ? !destination.equals(pathPoint.destination) : pathPoint.destination != null);
    }

    @Override
    public int hashCode() {
        int result = speed;
        result = 31 * result + (stayTime != +0.0f ? Float.floatToIntBits(stayTime) : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }
}
