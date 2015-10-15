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

    private float pause;

    int index;
    boolean forward;
    PathPoint targetPoint;

    public PathedBodyController(List<PathPoint> path, boolean pendulum) {
        this.path = path;
        this.pendulum = pendulum;

        index = -1;
        forward = true;
    }

    @Override
    public void update(float delta, BitBody body) {
        if (pause > 0) {
            body.velocity.set(0, 0);
            pause -= delta;

        } else {
            if (targetPoint == null) {
                pickNextPathPoint();
            }
            BitPoint difference = targetPoint.destination.minus(body.aabb.xy);
            if (Math.abs(difference.len()) < targetPoint.speed * delta) {
                // make it so we land perfectly on our target and then pause
                body.velocity.set(difference.scale(1/delta));
                pause = targetPoint.stayTime;
                targetPoint = null;
            } else {
                body.velocity.set(difference.normalize().scale(targetPoint.speed));
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
