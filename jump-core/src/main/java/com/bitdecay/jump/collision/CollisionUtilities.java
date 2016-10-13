package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.MathUtils;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.TileBody;

/**
 * Created by Monday on 6/16/2016.
 */
public class CollisionUtilities {
    public static BitPoint getRelativeMovement(BitBody body, BitBody otherBody) {
        // this line is just taking where the body tried to move and the partially resolved position into account to
        // figure out the relative momentum.
        BitPoint bodyAttempt = body.resolutionLocked ? new BitPoint(0, 0) : body.currentAttempt;
        BitPoint otherBodyAttempt = otherBody.resolutionLocked ? new BitPoint(0, 0) : otherBody.currentAttempt;
        return bodyAttempt.minus(otherBodyAttempt);
    }

    public static boolean canTileCollisionCanBeSkipped(TileBody tileBody, Manifold manifold, float resolutionPosition, float lastPosition) {
        if (!axisValidForNValue(manifold, tileBody)) {
            return true;
        }

        // XXX: This + 0.0001f is to account for minute floating point rounding issues that cause the raw math to be off
        // by tiny fractions. We want to basically account for these fractions and correctly enforce this case.
        // See that this fails: assertEquals(0.593822f, 32.593822f - 32.0f, 0);
        if (lastPosition + 0.001f < resolutionPosition) {
            // The actor was already inside the tileBody before it moved, so we can skip it.
            return true;
        }

        if (tileBody.collisionAxis != null) {
            return canSkipAxis(tileBody.collisionAxis, manifold);
        } else {
            return false;
        }
    }

    private static boolean canSkipAxis(BitPoint axis, Manifold manifold) {
        if (manifold.axis.equals(axis)) {
            return false;
        } else {
            return true;
        }
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
     * @param body the body being resolved
     * @param otherBody the other body that participated in the collision
     */
    public static Manifold solve(ManifoldBundle bundle, BitBody body, BitBody otherBody) {
        bundle.getCandidates().sort((o1, o2) -> Float.compare(Math.abs(o1.distance), Math.abs(o2.distance)));

        BitPoint relativeMovement = CollisionUtilities.getRelativeMovement(body, otherBody);

        float dotProd;
        for (Manifold manifold : bundle.getCandidates()) {
            dotProd = relativeMovement.dot(manifold.axis);
            if (dotProd != 0 && !MathUtils.sameSign(dotProd, manifold.distance)) {
                if (otherBody instanceof TileBody) {
                    // confirm that body came from past this thing

                    // use the relative position between the centers of the bodies
                    float resolutionDistance = body.aabb.center().minus(otherBody.aabb.center()).plus(manifold.result).dot(manifold.axis);
                    float lastDistance = body.aabb.center().minus(body.currentAttempt).minus(otherBody.aabb.center()).dot(manifold.axis);

                    if (CollisionUtilities.canTileCollisionCanBeSkipped((TileBody) otherBody, manifold, resolutionDistance, lastDistance)) {
                        continue;
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

    public static boolean axisValidForNValue(Manifold axisOver, TileBody body) {
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
