package com.bitdecay.jump.geom;

/**
 * Created by Monday on 10/11/2015.
 */
public class PathPoint {
    public BitPoint destination;
    public float stayTime;

    public PathPoint() {
        // Here for JSON
    }

    public PathPoint(BitPoint point, float stay) {
        destination = point;
        stayTime = stay;
    }
}
