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
                targetPoint = pickNextPathPoint(true);
            }
            BitPoint difference = targetPoint.destination.minus(body.aabb.xy);
            float distanceToGo = Math.abs(difference.len());
            float travelThisFrame = targetPoint.speed * delta;
            if (distanceToGo < travelThisFrame) {
                // Find how far along the next path we should have moved because of overshooting our current target
                float remainder = travelThisFrame - distanceToGo;
                float percent = remainder / travelThisFrame;
                float normalizedTravel = pickNextPathPoint(false).speed * delta * percent;
                BitPoint trueMovement = difference.plus(pickNextPathPoint(false).destination.minus(targetPoint.destination).normalize().scale(normalizedTravel));
                body.velocity.set(trueMovement.scale(1 / delta));
                pause = targetPoint.stayTime;
                targetPoint = null;
            } else {
                body.velocity.set(difference.normalize().scale(targetPoint.speed));
            }
        }
    }

    private PathPoint pickNextPathPoint(boolean updateIndex) {
        int nextIndex = index;
        if (forward) {
            if (nextIndex + 1 == path.size()) {
                if (pendulum) {
                    nextIndex--;
                    forward = false;
                } else {
                    nextIndex = 0;
                }
            } else {
                nextIndex++;
            }
        } else {
            if (index - 1 == -1) {
                nextIndex++;
                forward = true;
            } else {
                nextIndex--;
            }
        }
        if (updateIndex) {
            index = nextIndex;
        }
        return path.get(nextIndex);
    }
}
