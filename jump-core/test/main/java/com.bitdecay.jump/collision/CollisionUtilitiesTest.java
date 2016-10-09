package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.TileBody;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Monday on 6/19/2016.
 */
public class CollisionUtilitiesTest {
    @Test
    public void testGetRelativeMovement() {
        BitBody bodyOne = new BitBody();
        bodyOne.currentAttempt = new BitPoint(1, 1);

        BitBody bodyTwo = new BitBody();
        bodyTwo.currentAttempt = new BitPoint(-1, 1);

        BitPoint relativeMovement = CollisionUtilities.getRelativeMovement(bodyOne, bodyTwo, new BitPoint());

        assertEquals(2, relativeMovement.x, 0);
        assertTrue(relativeMovement.y == 0);
    }

    @Test
    public void testRelativeMovementWithCumulative() {
        BitBody bodyOne = new BitBody();
        bodyOne.currentAttempt = new BitPoint(1, 1);

        BitBody bodyTwo = new BitBody();
        bodyTwo.currentAttempt = new BitPoint(-1, 1);

        BitPoint relativeMovement = CollisionUtilities.getRelativeMovement(bodyOne, bodyTwo, new BitPoint(.5f, 0));

        assertEquals(2.5, relativeMovement.x, 0);
        assertEquals(0, relativeMovement.y, 0);
    }

    // TODO: This test warrants a revisit on the desired behavior of resolution locked works
    // with the returned value of relative movement
    @Test
    public void testRelativeMovementWithResolutionLock() {
        BitBody bodyOne = new BitBody();
        bodyOne.currentAttempt = new BitPoint(1, 1);

        BitBody bodyTwo = new BitBody();
        bodyTwo.currentAttempt = new BitPoint(-1, 1);
        bodyTwo.resolutionLocked = true;

        BitPoint relativeMovement = CollisionUtilities.getRelativeMovement(bodyOne, bodyTwo, new BitPoint());

        assertEquals(1, relativeMovement.x, 0);
        assertEquals(1, relativeMovement.y, 0);
    }

    @Test
    public void testAxisValidForNValue() {
        TileBody nothingAround = new TileBody();
        nothingAround.nValue = 0;

        Manifold downManifold = new Manifold(new BitPoint(0, -1), 1);
        assertTrue(CollisionUtilities.axisValidForNValue(downManifold, nothingAround));

        Manifold upManifold = new Manifold(new BitPoint(0, 1), 1);
        assertTrue(CollisionUtilities.axisValidForNValue(upManifold, nothingAround));

        Manifold leftManifold = new Manifold(new BitPoint(-1, 0), 1);
        assertTrue(CollisionUtilities.axisValidForNValue(leftManifold, nothingAround));

        Manifold rightManifold = new Manifold(new BitPoint(1, 0), 1);
        assertTrue(CollisionUtilities.axisValidForNValue(rightManifold, nothingAround));
    }

    @Test
    public void testAxisNotValidForNValue() {
        TileBody nothingAround = new TileBody();
        nothingAround.nValue = Direction.UP;
        assertTrue(areAllDirectionsValidBut(nothingAround, Direction.UP));

        nothingAround.nValue = Direction.DOWN;
        assertTrue(areAllDirectionsValidBut(nothingAround, Direction.DOWN));

        nothingAround.nValue = Direction.RIGHT;
        assertTrue(areAllDirectionsValidBut(nothingAround, Direction.RIGHT));

        nothingAround.nValue = Direction.LEFT;
        assertTrue(areAllDirectionsValidBut(nothingAround, Direction.LEFT));
    }

    /**
     * Helper method to test directional validity
     * @param body
     * @param dir
     * @return
     */
    private static boolean areAllDirectionsValidBut(TileBody body, int dir) {
        int result = 0;
        Manifold downManifold = new Manifold(new BitPoint(0, -1), 1);
        if (CollisionUtilities.axisValidForNValue(downManifold, body)) {
            result |= Direction.DOWN;
        }

        Manifold upManifold = new Manifold(new BitPoint(0, 1), 1);
        if (CollisionUtilities.axisValidForNValue(upManifold, body)) {
            result |= Direction.UP;
        }

        Manifold leftManifold = new Manifold(new BitPoint(-1, 0), 1);
        if (CollisionUtilities.axisValidForNValue(leftManifold, body)) {
            result |= Direction.LEFT;
        }

        Manifold rightManifold = new Manifold(new BitPoint(1, 0), 1);
        if (CollisionUtilities.axisValidForNValue(rightManifold, body)) {
            result |= Direction.RIGHT;
        }

        if ((result & dir) == 0 && (result | dir) == Direction.ALL) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void testTileCollisionCanBeSkippedNValue() {
        TileBody body = new TileBody();
        body.nValue = 15;
        Manifold manifold = new Manifold(new BitPoint(1, 0), 1);
        float resPosition = 0;
        float lastPosition = 1;
        boolean canBeSkipped = CollisionUtilities.canTileCollisionCanBeSkipped(body, manifold, resPosition, lastPosition);
        assertTrue(canBeSkipped);
    }

    @Test
    public void testTileCollisionsCanBeSkippedWithoutAxis() {
        TileBody body = new TileBody();
        Manifold manifold = new Manifold(new BitPoint(1, 0), 1);
        float resPosition = 0;
        float lastPosition = 1;
        boolean canBeSkipped = CollisionUtilities.canTileCollisionCanBeSkipped(body, manifold, resPosition, lastPosition);
        assertFalse(canBeSkipped);
    }

    @Test
    public void testTileCollisionsCanBeSkippedWithAxis() {
        TileBody body = new TileBody();
        body.collisionAxis = new BitPoint(1, 0);
        Manifold manifold = new Manifold(new BitPoint(1, 0), 1);
        float resPosition = 0;
        float lastPosition = 1;

        boolean canBeSkipped = CollisionUtilities.canTileCollisionCanBeSkipped(body, manifold, resPosition, lastPosition);
        assertFalse(canBeSkipped);

        body.collisionAxis = new BitPoint(-1, 0);
        canBeSkipped = CollisionUtilities.canTileCollisionCanBeSkipped(body, manifold, resPosition, lastPosition);
        assertTrue(canBeSkipped);
    }
}
