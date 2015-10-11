package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;

import java.util.List;

/**
 * Created by Monday on 10/8/2015.
 */
public class PathedBodyController implements BitBodyController{
    public List<BitPoint> path;
    public boolean pendulum;
    private float speed;

    int index;
    boolean forward;
    BitPoint targetPoint;

    public PathedBodyController(List<BitPoint> path, boolean pendulum, float speed) {
        this.path = path;
        this.pendulum = pendulum;
        this.speed = speed;

        index = 0;
        forward = true;
    }

    @Override
    public void update(float delta, BitBody body) {
        if (targetPoint == null) {
            pickNextPathPoint();
        }
        BitPoint difference = targetPoint.minus(body.aabb.xy);
        body.velocity.set(difference.normalize().scale(speed));
        if (Math.abs(difference.len()) < speed * delta) {
            targetPoint = null;
        }
    }

    private void pickNextPathPoint() {
        if (forward) {
            if (index + 1 == path.size()) {
                if (pendulum) {
                    index--;
                    forward = false;
                } else {
                    index = 0;
                }
            } else {
                index++;
            }
        } else {
            if (index - 1 == -1) {
                index++;
                forward = true;
            } else {
                index--;
            }
        }
        targetPoint = path.get(index);
    }
}
