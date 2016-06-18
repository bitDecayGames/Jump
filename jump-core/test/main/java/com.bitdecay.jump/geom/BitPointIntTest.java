package com.bitdecay.jump.geom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Monday on 6/12/2016.
 */
public class BitPointIntTest {

    @Test
    public void testCreate() {
        new BitPointInt();

        BitPointInt point = new BitPointInt(0, 0);
        assertNotNull(point);
    }

    @Test
    public void testMinus() {
        BitPointInt point = new BitPointInt(10, 10);

        BitPointInt other = new BitPointInt(5, 5);

        BitPointInt result = point.minus(other);

        assertTrue(result.x == 5);
        assertTrue(result.y == 5);
    }

    @Test
    public void testDivide() {
        BitPointInt point = new BitPointInt(10, 10);

        BitPointInt result = point.floorDivideBy(3, 3);

        assertTrue(result.x == 3);
        assertTrue(result.y == 3);
    }

    @Test
    public void testEquals() {
        BitPointInt point = new BitPointInt(10, 10);
        assertTrue(point.equals(point));

        BitPointInt other = new BitPointInt(10, 10);
        assertTrue(point.equals(other));

        other.x = 9;
        assertFalse(point.equals(other));

        assertFalse(point.equals(new String()));
    }
}
