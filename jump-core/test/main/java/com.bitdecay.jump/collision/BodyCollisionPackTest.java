package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 9/5/2016.
 */
public class BodyCollisionPackTest {

    @Test
    public void testFindTrueCollisions() {
        BitBody actor = new BitBody();
        actor.aabb = new BitRectangle(0, 0, 10, 10);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(9, 9, 10, 10);


        pack.suspects.add(sus1);

        pack.findTrueCollisions();

        assertEquals(1, pack.allResolutions.size());

        assertEquals(sus1, pack.allResolutions.get(0).with);
    }

    @Test
    public void testFindTrueCollisionsWhenNone() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 0, 10, 10);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(11, 0, 10, 10);


        pack.suspects.add(sus1);

        pack.findTrueCollisions();

        assertEquals(0, pack.allResolutions.size());
    }

    @Test
    public void testFindActionableCollisionsHappyPath() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 0, 10, 10);
        actor.currentAttempt.set(1, 0);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(9, 5, 10, 10);
        pack.suspects.add(sus1);

        pack.findTrueCollisions();
        assertEquals(1, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(1, pack.actionableResolutions.size());
        assertEquals(sus1, pack.actionableResolutions.get(0).with);
    }

    @Test
    public void testFindActionableCollisionsWhenCollideFalse() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 0, 10, 10);
        actor.props.collides = false;

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(9, 9, 10, 10);
        pack.suspects.add(sus1);

        pack.findTrueCollisions();
        assertEquals(1, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(0, pack.actionableResolutions.size());
    }

    @Test
    public void testFindActionableCollisionsWhenResolutionLocked() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;
        actor.aabb = new BitRectangle(0, 0, 10, 10);
        actor.resolutionLocked = true;

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(9, 9, 10, 10);
        pack.suspects.add(sus1);

        pack.findTrueCollisions();
        assertEquals(1, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(0, pack.actionableResolutions.size());
    }

    @Test
    public void testFindActionableCollisionsIgnoresZeroAxis() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 0, 10, 10);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.bodyType = BodyType.STATIC;

        sus1.aabb = new BitRectangle(9, 9, 10, 10);
        pack.suspects.add(sus1);

        ManifoldBundle bundle = new ManifoldBundle();
        bundle.addCandidate(new Manifold(new BitPoint(), 0));

        pack.allResolutions.add(new BodyCollisionPack.Overlap(sus1, bundle));

        pack.filterActionableResolutions();
        assertEquals(0, pack.actionableResolutions.size());
    }

    @Test
    public void testFindActionableCollisionsIgnoresDynamics() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 0, 10, 10);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.bodyType = BodyType.DYNAMIC;

        sus1.aabb = new BitRectangle(9, 9, 10, 10);
        pack.suspects.add(sus1);

        pack.findTrueCollisions();
        assertEquals(1, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(0, pack.actionableResolutions.size());
    }

    @Test
    public void testSetCumulativeResolution() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(1, 1, 10, 10);
        actor.currentAttempt.set(1, 1);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(10, 0, 10, 10);
        pack.suspects.add(sus1);

        BitBody sus2 = new BitBody();
        sus2.aabb = new BitRectangle(0, 10, 10, 10);
        pack.suspects.add(sus2);

        pack.findTrueCollisions();
        assertEquals(2, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(2, pack.actionableResolutions.size());

        pack.setCumulativeResolution();
        assertEquals(new BitPoint(-1, -1), pack.neededResolution);
    }

    @Test
    public void testSetCumulativeResolutionCrush() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(1, 1, 10, 10);
        actor.currentAttempt.set(1, 1);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(10, 0, 10, 10);
        pack.suspects.add(sus1);

        BitBody sus2 = new BitBody();
        sus2.bodyType = BodyType.KINETIC;
        sus2.aabb = new BitRectangle(-8, 0, 10, 10);
        sus2.currentAttempt.set(2, 0);
        pack.suspects.add(sus2);

        pack.findTrueCollisions();
        assertEquals(2, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(2, pack.actionableResolutions.size());

        pack.setCumulativeResolution();
        assertTrue(pack.resultsInCrush);
    }

    @Test
    public void testSetCumulativeResolutionParent() {
        BitBody actor = new BitBody();
        actor.bodyType = BodyType.DYNAMIC;

        actor.aabb = new BitRectangle(0, 1, 10, 10);
        actor.currentAttempt.set(1, 1);

        BitWorld world = new BitWorld();
        world.setGravity(0, -100);
        BodyCollisionPack pack = new BodyCollisionPack(actor, world);

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(0, 0, 10, 3);
        sus1.bodyType = BodyType.KINETIC;
        sus1.currentAttempt.set(0, 2);
        pack.suspects.add(sus1);

        pack.findTrueCollisions();
        assertEquals(1, pack.allResolutions.size());

        pack.filterActionableResolutions();
        assertEquals(1, pack.actionableResolutions.size());

        pack.setCumulativeResolution();
        assertEquals(1, pack.resultingParents.size());

        assertEquals(sus1, pack.resultingParents.get(0));
    }
}
