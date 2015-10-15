package com.bitdecay.jump.geom;

/**
 * Created by Monday on 10/11/2015.
 */
public class PathPoint {
    public BitPoint destination;
    public float speed;
    public float stayTime;

    public PathPoint() {
        // Here for JSON
    }

    public PathPoint(BitPoint point, float speed, float stay) {
        destination = point;
        this.speed = speed;
        stayTime = stay;
    }
}
