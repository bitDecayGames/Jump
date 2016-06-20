package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
