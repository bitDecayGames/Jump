package com.bitdecay.jump.collision;

import com.bitdecay.jump.*;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Monday on 9/27/2015.
 */
public class SATStrategy {

    public PriorityQueue<BitCollision> collisions = new PriorityQueue<>();

    protected BitPoint cumulativeResolution;
    protected BitPoint resolution = new BitPoint(0, 0);
    protected BitBody body;

    /**
     * If set to true, {@link BitBody#resolutionLocked} should be flagged as true after this resolution
     */
    public boolean lockingResolution;

    public SATStrategy(BitBody body) {
        this.body = body;
        cumulativeResolution = new BitPoint();
    }

    /**
     * Determines appropriate resolution for the body based on the collisions found. This method will
     * deactivate the body if opposing collisions are found.
     * @param world
     */
    public void satisfy(BitWorld world) {
        // TODO: This method can probably made more efficient if needed down the road
        List<BitPoint> directionsResolved = new ArrayList<>();
        for (BitCollision collision : collisions) {
            Manifold manifold = getSolution(cumulativeResolution, collision);
            if (manifold.axis.equals(GeomUtils.ZERO_AXIS)) {
                continue;
            }
            if (BodyType.STATIC.equals(collision.against.bodyType) ||
                    BodyType.KINETIC.equals(collision.against.bodyType) ||
                    collision.against.resolutionLocked) {
                this.lockingResolution = true;
            }
            cumulativeResolution.add(manifold.result);
            BitPoint resAxis = manifold.result.normalize();
            for (BitPoint otherAxis : directionsResolved) {
                // this will only check exact opposites, may need to change if we move away from rectangle tiles
                if (collision.body.props.crushable && resAxis.scale(-1).equals(otherAxis)) {
                    // reset any pending resolutions.
                    // don't bother moving a dead body
                    // short circuit the resolution
                    // and return
                    cumulativeResolution.set(body.aabb.xy);
                    body.active = false;
                    body.velocity.set(0, 0);
                    body.getContactListeners().forEach(listener -> listener.crushed());
                    return;
                }
            }
            directionsResolved.add(resAxis);
            postResolve(world, body, collision.against, manifold);
        }
        // set final resolution values
        resolution.set(cumulativeResolution);
    }

    /**
     * Adjusts the resolved position based on both the outcome of computing the resolution axis AND
     * player properties such as the max angle they can walk up.
     * @param cumulativeResolution partially built resolved position
     * @param collisionBundle the collision to take into consideration
     */
    private Manifold getSolution(BitPoint cumulativeResolution, BitCollision collisionBundle) {
        BitRectangle effectiveSpace = new BitRectangle(collisionBundle.body.aabb);
        effectiveSpace.xy.add(cumulativeResolution);
        SATCollision collision = SATUtilities.getCollision(effectiveSpace, collisionBundle.against.aabb);
        if (collision != null) {
            Manifold candidate = collision.solve(body, collisionBundle.against, cumulativeResolution);
            if (candidate.axis.x != 0 && candidate.axis.y > 0) {
                // this is logic to make it so the player doesn't move slower when running uphill. Likewise, we will need logic to 'glue' the player to the ground when running downhill.
                // atan is our angle of resolution
                double atan = Math.atan(candidate.axis.y / candidate.axis.x);

                if (Math.abs(atan - MathUtils.PI_OVER_TWO) <= Math.toRadians(30)) {
                    // currently we impose a hard limit of 30 degree angle 'walkability'
                    if (atan > 0) {
                        double angleToUpright;
                        angleToUpright = MathUtils.PI_OVER_TWO - atan;
                        double straightUp = candidate.distance / Math.cos(angleToUpright);
                        cumulativeResolution.add(0, (float) straightUp);
                    } else {
                        double angleToUpright;
                        angleToUpright = -MathUtils.PI_OVER_TWO - atan;
                        double straightUp = candidate.distance / Math.cos(angleToUpright);
                        cumulativeResolution.add(0, (float) straightUp);
                    }
                    return candidate;
                } else {
                    // TODO: we need to actually resolve the player horizontally, showing the angle is too steep.
                }
            }
            return candidate;
        } else {
            return GeomUtils.ZERO_MANIFOLD;
        }
    }

    /**
     * Handles any interactions that may result from the collision. Things such as parents-child (when a body is
     * being moved by another body) bonds are enforced here.
     * @param world the world
     * @param body the body being resolved
     * @param otherBody the body they collided against
     * @param manifold
     */
    private void postResolve(BitWorld world, BitBody body, BitBody otherBody, Manifold manifold) {
        if (BodyType.KINETIC.equals(otherBody.bodyType)) {
            // attach if we were resolved against gravity (aka we are standing on it)
            if (manifold.axis.dot(world.gravity.x, world.gravity.y) < 0) {
                body.parents.add(otherBody);
                otherBody.children.add(body);
            }
        }
    }



}
