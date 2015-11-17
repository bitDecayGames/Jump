package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.MathUtils;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.TileBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monday on 9/6/2015.
 */
public class SATCollision {
    private List<Manifold> manifoldCandidates = new ArrayList<>();

    public void addCandidate(Manifold manifold) {
        if (manifold.distance < 0) {
            manifold = new Manifold(manifold.axis.times(-1), manifold.distance * -1);
        }
        manifoldCandidates.add(manifold);
    }

    /**
     * This method finds what axis should be used for resolution. This method takes into account the relative
     * speeds of the objects being resolved. A resolution will only be applied to an object that is AGAINST
     * the relative movement between it and what it has collided against.
     *
     * This logic is here to ensure corners feel crisp when a body is landing on the edge of a platform.
     *
     * <b>NOTE:</b> Due to logic to make sure the feel is correct, there are situations (such as one-way
     * platforms) that can result in NO axis (aka the Zero-axis) being set on the resolution.
     *
     *  @param body the body being resolved
     * @param otherBody the other body that participated in the collision
     * @param resolvedPosition the current partially built resolution
     */
    public Manifold solve(BitBody body, BitBody otherBody, BitRectangle resolvedPosition) {
        manifoldCandidates.sort((o1, o2) -> Float.compare(Math.abs(o1.distance), Math.abs(o2.distance)));
        // this line is just taking where the body tried to move and the partially resolved position into account to
        // figure out the relative momentum.
        BitPoint relativeMovement = body.currentAttempt.plus(resolvedPosition.xy.minus(body.aabb.xy)).minus(otherBody.currentAttempt);
        float dotProd;
        for (Manifold manifold : manifoldCandidates) {
            dotProd = relativeMovement.dot(manifold.axis);
            if (dotProd != 0 && !MathUtils.sameSign(dotProd, manifold.distance)) {
                if (otherBody instanceof TileBody) {
                    if (!axisValidForNValue(manifold, (TileBody) otherBody)) {
                        continue;
                    }
                    // confirm that body came from past this thing
                    float resolutionPosition = resolvedPosition.xy.plus(manifold.axis.times(manifold.distance)).dot(manifold.axis);
                    float lastPosition = body.lastPosition.dot(manifold.axis);

                    if (MathUtils.opposing(resolutionPosition, lastPosition)) {
                        // all collisions should push a body backwards according to the
                        // relative movement. If it's not doing so, it's not a valid case.
                        continue;
                    }

                    if (manifold.distance < 0 && (lastPosition > resolutionPosition)) {
                        continue;
                    }

                    if (manifold.distance > 0 && lastPosition < resolutionPosition) {
                        continue;
                    }

                    if (((TileBody) otherBody).collisionAxis != null) {
                        if (manifold.axis.equals(((TileBody) otherBody).collisionAxis)) {
                            if (manifold.distance < 0) {
                                continue;
                            }
                        } else if (manifold.axis.equals(((TileBody) otherBody).collisionAxis.times(-1))) {
                            if (manifold.distance > 0) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                return manifold;
            }
        }
        /*
         * This is a fallback, I can't think of a scenario where we would get here, but better to do
         * something than to leave ourselves open to an NPE
         */
        return GeomUtils.ZERO_MANIFOLD;
    }

    private boolean axisValidForNValue(Manifold axisOver, TileBody body) {
        if (axisOver.axis.equals(GeomUtils.X_AXIS) && (body.nValue & Direction.RIGHT) == 0) {
            return true;
        } else if (axisOver.axis.equals(GeomUtils.NEG_X_AXIS) && (body.nValue & Direction.LEFT) == 0) {
            return true;
        } else if (axisOver.axis.equals(GeomUtils.Y_AXIS) && (body.nValue & Direction.UP) == 0) {
                return true;
        } else if (axisOver.axis.equals(GeomUtils.NEG_Y_AXIS) && (body.nValue & Direction.DOWN) == 0) {
                return true;
        } else {
            return false;
        }
    }
}

