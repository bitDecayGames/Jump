package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import org.junit.Test;
import static org.junit.Assert.*;

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
}
