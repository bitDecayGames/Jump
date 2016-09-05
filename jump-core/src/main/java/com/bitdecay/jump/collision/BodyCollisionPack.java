package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.GeomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds an actor (the body we are concerned with) and a list of suspects (bodies we *might* be colliding with)
 * Created by Monday on 9/5/2016.
 */
public class BodyCollisionPack {
    /**
     * The body of focus
     */
    final BitBody actor;

    /**
     * A reference to the world this pack was created by
     */
    private BitWorld world;

    /**
     * All bodies that we believe might be colliding with our actor
     */
    final List<BitBody> suspects;

    /**
     * All resolutions, including any ignorable collisions such as one-way platforms and sensor bodies.
     */
    final List<Overlap> allResolutions;

    /**
     * A subset of allResolutions containing the proposed resolutions that are valid collisions to resolve
     */
    final List<Overlap> actionableResolutions;

    /**
     * A vector which will satisfy all actionableResolutions
     */
    final BitPoint neededResolution;

    /**
     * A flag to indicate if this pack had opposing resolutions
     */
    boolean resultsInCrush = false;

    /**
     * A flag to indicate if the body should stop being resolved for the rest of the world step after this has
     * been applied
     */
    boolean lockingResolution = false;


    public BodyCollisionPack(BitBody actor, BitWorld world) {
        this.actor = actor;
        this.world = world;
        suspects = new ArrayList<>();
        allResolutions = new ArrayList<>();
        actionableResolutions = new ArrayList<>();
        neededResolution = new BitPoint();
    }

    public void findTrueCollisions() {
        for (BitBody suspect : suspects) {
            Manifold candidate = CollisionUtilities.getSolutionCandidate(actor, suspect, new BitPoint());
            if (candidate.axis.equals(GeomUtils.ZERO_AXIS)) {
                continue;
            } else {
                allResolutions.add(new Overlap(suspect, candidate));
            }
        }
    }

    public void filterActionableResolutions() {
        if (actor.props.collides == false || actor.resolutionLocked) {
            return;
        }

        for (Overlap overlap : allResolutions) {
            if (overlap.with.bodyType.equals(BodyType.DYNAMIC)) {
                continue;
            }
            if (overlap.with.props.collides) {
                actionableResolutions.add(overlap);
            }
        }
    }

    public void setCumulativeResolution() {
        List<BitPoint> requiredResolutionAxes = new ArrayList<>();
        BitPoint cumulativeResolution = new BitPoint();

        for (Overlap overlap : actionableResolutions) {
            if (Math.abs(overlap.resolutionManifold.result.x) > Math.abs(cumulativeResolution.x)) {
                cumulativeResolution.x = overlap.resolutionManifold.result.x;
            }
            if (Math.abs(overlap.resolutionManifold.result.y) > Math.abs(cumulativeResolution.y)) {
                cumulativeResolution.y = overlap.resolutionManifold.result.y;
            }
            BitPoint resAxis = overlap.resolutionManifold.result.normalize();
            for (BitPoint otherAxis : requiredResolutionAxes) {
                if (resAxis.dot(otherAxis) < 0) {
                    // The resolutions are opposed to each other.
                    // This is an invalid scenario. Set our flag and bail early
                    resultsInCrush = true;
                    return;
                }
            }

            if (overlap.with.bodyType.equals(BodyType.KINETIC)) {
                if (overlap.resolutionManifold.axis.dot(world.gravity.x, world.gravity.y) < 0) {
                    actor.parents.add(overlap.with);
                    overlap.with.children.add(actor);
                }
            }

            lockingResolution |= overlap.with.bodyType.equals(BodyType.STATIC);

            requiredResolutionAxes.add(resAxis);
        }

        neededResolution.set(cumulativeResolution);
    }


    public static class Overlap {
        BitBody with;
        Manifold resolutionManifold;

        public Overlap(BitBody suspect, Manifold candidate) {
            this.with = suspect;
            this.resolutionManifold = candidate;
        }
    }
}
