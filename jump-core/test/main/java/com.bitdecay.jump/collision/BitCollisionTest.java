package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/8/2016.
 */
public class BitCollisionTest {

    @Test
    public void testCanBeIgnoredReturnsFalse() {
        BitBody bodyOne = new BitBody();
        bodyOne.props.collides = true;

        BitBody bodyTwo = new BitBody();
        bodyTwo.props.collides = true;
        BitCollision col = new BitCollision(bodyOne, bodyTwo);

        assertFalse(col.canBeIgnored());
    }

    @Test
    public void testCanBeIgnoredReturnsTrueBasedOnCollidesProp() {
        BitBody bodyOne = new BitBody();
        bodyOne.props.collides = false;

        BitBody bodyTwo = new BitBody();
        bodyTwo.props.collides = true;
        BitCollision col = new BitCollision(bodyOne, bodyTwo);

        assertTrue(col.canBeIgnored());

        bodyOne.props.collides = true;
        bodyTwo.props.collides = false;

        assertTrue(col.canBeIgnored());

        bodyTwo.props.collides = false;

        assertTrue(col.canBeIgnored());
    }

    @Test
    public void testCanBeIgnoredReturnsTrueBasedOnResolutionLocked() {
        BitBody bodyOne = new BitBody();
        bodyOne.props.collides = true;
        bodyOne.resolutionLocked = true;

        BitBody bodyTwo = new BitBody();
        bodyTwo.props.collides = true;
        BitCollision col = new BitCollision(bodyOne, bodyTwo);
        assertTrue(col.canBeIgnored());
    }

    @Test
    public void testBodyOrderPriority() {
        BitBody bodyOne = new BitBody();
        bodyOne.bodyType = BodyType.DYNAMIC;

        BitBody bodyTwo = new BitBody();
        bodyTwo.bodyType = BodyType.STATIC;

        BitBody bodyThree = new BitBody();
        bodyThree.bodyType = BodyType.KINETIC;

        BitCollision againstDynamic = new BitCollision(bodyOne, bodyOne);
        BitCollision againstStatic = new BitCollision(bodyOne, bodyTwo);
        BitCollision againstKinetic = new BitCollision(bodyOne, bodyThree);

        assertTrue(againstDynamic.compareTo(againstDynamic) == 0);
        assertTrue(againstDynamic.compareTo(againstStatic) < 0);
        assertTrue(againstDynamic.compareTo(againstKinetic) < 0);

        assertTrue(againstStatic.compareTo(againstDynamic) > 0);
        assertTrue(againstStatic.compareTo(againstStatic) == 0);
        assertTrue(againstStatic.compareTo(againstKinetic) > 0);

        assertTrue(againstKinetic.compareTo(againstDynamic) > 0);
        assertTrue(againstKinetic.compareTo(againstStatic) < 0);
        assertTrue(againstKinetic.compareTo(againstKinetic) == 0);
    }
}
