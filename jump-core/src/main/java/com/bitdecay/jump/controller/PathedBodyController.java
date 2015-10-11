package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.PathPoint;

import java.util.List;

/**
 * Created by Monday on 10/8/2015.
 */
public class PathedBodyController implements BitBodyController{
    public List<PathPoint> path;
    public boolean pendulum;
    private float speed;

    private float pause;

    int index;
    boolean forward;
    PathPoint targetPoint;

    public PathedBodyController(List<PathPoint> path, boolean pendulum, float speed) {
        this.path = path;
        this.pendulum = pendulum;
        this.speed = speed;

        index = 0;
        forward = true;
    }

    @Override
    public void update(float delta, BitBody body) {
        if (pause > 0) {
            pause -= delta;

        } else {
            if (targetPoint == null) {
                pickNextPathPoint();
            }
            BitPoint difference = targetPoint.destination.minus(body.aabb.xy);
            if (Math.abs(difference.len()) < speed * delta) {
                body.velocity.set(0, 0);
                body.aabb.xy.set(targetPoint.destination);
                pause = targetPoint.stayTime;
                targetPoint = null;
            } else {
                body.velocity.set(difference.normalize().scale(speed));
            }
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
