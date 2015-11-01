package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
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

    private float extraPercent;

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
            BitPoint distanceToDestination = targetPoint.destination.minus(body.aabb.xy);
            float distanceToGo = Math.abs(distanceToDestination.len());
            float travelThisFrame = targetPoint.speed * delta;
            if (distanceToGo < travelThisFrame) {
                // Find how far along the next path we should have moved because of overshooting our current target
                float distanceOvershot = travelThisFrame - distanceToGo;
                extraPercent = distanceOvershot / travelThisFrame;
                /**
                 * We set velocity instead of position so the engine can do the actual move. But in doing this
                 * we have to adjust the velocity based on the delta to make sure we move the right distance
                 * next update.
                 */
                body.velocity.set(distanceToDestination.scale(BitWorld.STEP_PER_SEC));
                pause = targetPoint.stayTime;
                targetPoint = null;
            } else {
                body.velocity.set(distanceToDestination.normalize().scale(targetPoint.speed * (1 + extraPercent)));
                extraPercent = 0;
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
